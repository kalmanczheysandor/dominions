package hu.kalmancheysandor.applications.dominions.servers.ai.liz.agent.service;


import hu.kalmancheysandor.applications.dominions.apis.ai.engine.liz.LizAiEngine;
import hu.kalmancheysandor.applications.dominions.apis.ai.engine.liz.LizNeuralNetwork;
import hu.kalmancheysandor.applications.dominions.apis.ai.neural.INeuralAiEngine;
import hu.kalmancheysandor.applications.dominions.apis.ai.neural.INeuralNetwork;

import hu.kalmancheysandor.applications.dominions.apis.server.ai.common.dto.AiDecisionRequest;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.common.dto.AiDecisionResponse;

import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.entity.history.LizHistoryPlayer;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.entity.history.LizHistoryScenario;

import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.entity.training.LizCharacter;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.entity.training.LizVariant;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.entity.training.neural.LizNeuralConcept;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.entity.training.neural.LizNeuralExecution;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.exception.character.LizCharacterAssociationRestrictedException;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.exception.character.LizCharacterNotFoundByCodeException;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.exception.character.LizCharacterNotFoundByUuidException;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.exception.concept.LizNeuralConceptAssociationRestrictedException;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.exception.concept.LizNeuralConceptNoFinishedExecutionExistsException;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.exception.concept.LizNeuralConceptNotFoundException;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.exception.variant.LizVariantAssociationRestrictedException;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.exception.variant.LizVariantNotFoundException;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.repository.history.LizHistoryPlayerRepository;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.repository.history.LizHistoryRepository;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.repository.history.LizHistoryScenarioRepository;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.repository.history.LizHistorySessionRepository;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.repository.training.LizCharacterRepository;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.repository.training.neural.LizNeuralExecutionRepository;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.repository.training.neural.LizNeuralTrainingResultRepository;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.service.TLizService;
//import hu.kalmancheysandor.applications.dominions.apis.server.game.common.repository.game.GameScenarioRepository;
import hu.kalmancheysandor.applications.dominions.apis.util.file.filehandler.FileHandler;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.*;

@Slf4j
@Service
@Transactional
public class LizAgentService extends TLizService {

    @Autowired
    private LizHistoryPlayerRepository lizHistoryPlayerRepository;

    @Autowired
    private LizHistoryScenarioRepository lizHistoryScenarioRepository;

    @Autowired
    private LizNeuralExecutionRepository lizNeuralExecutionRepository;

    @Autowired
    private LizNeuralTrainingResultRepository lizNeuralTrainingResultRepository;

//    @Autowired
//    private GameScenarioRepository gameScenarioRepository;

    @Autowired
    private LizCharacterRepository lizCharacterRepository;

    @Autowired
    private LizHistoryRepository lizHistoryRepository;


    @Autowired
    private LizHistorySessionRepository lizHistorySessionRepository;


    public AiDecisionResponse generateResponse(AiDecisionRequest request) {

        //
        String sessionUuid = request.getSessionUuid();
        String scenarioUuid = request.getScenarioUuid();
        String scenarioName = request.getScenarioName();
        String userUuid = request.getYourUserUuid();
        String userName = request.getYourUserName();
        String myCharacterCode = request.getPlayerCharacterCode();
        Map<Integer, AiDecisionRequest.Player> enemyPlayers = request.getEnemyPlayers();

        // Test whether working-profile (=character) is existing and accessible/enabled
        assertCharacterExistsAndIsEnabledByCharacterCode(myCharacterCode);

        // Register local representations if not exist
        registerHistorySessionIfNotExists(sessionUuid);
        registerHistoryPlayerIfNotExists(userUuid, userName);
        registerHistoryScenarioIfNotExists(scenarioUuid, scenarioName);

        // Access local representation
        LizHistoryScenario historyScenario = accessHistoryScenario(scenarioUuid);
        int historyScenarioId = historyScenario.getId();

        // Find entities
        int conceptId = determineConceptIdFromCharacterCode(myCharacterCode);
        int latestExecutionId = determineLatestFinishedExecution(conceptId);

        // Initialise the decision-making engine
        INeuralAiEngine aiDecisionEngine = new LizAiEngine();

        // Register all enemy network
        for (Map.Entry<Integer, AiDecisionRequest.Player> playerEntry : enemyPlayers.entrySet()) {
            AiDecisionRequest.Player enemyPlayer = playerEntry.getValue();

            // Register and access local representation of enemy player
            registerHistoryPlayerIfNotExists(enemyPlayer.getUserUuid(), enemyPlayer.getName());
            LizHistoryPlayer lizHistoryPlayer = accessHistoryPlayer(enemyPlayer.getUserUuid());
            int enemyPlayerId = lizHistoryPlayer.getId();


            INeuralNetwork enemyNeuralNetwork = accessNeuralNetwork(conceptId, historyScenarioId, enemyPlayerId, latestExecutionId);
            aiDecisionEngine.registerEnemyNetwork(enemyPlayer.getUserUuid(), enemyNeuralNetwork);
        }

        System.out.println("RESPONSE GENERATED:Scenario:" + historyScenarioId);

        // Generate decision and response
        return convertDecisionResultToResponse(
                aiDecisionEngine.makeDecision(
                        convertRequestToDecisionContext(request)
                )
        );
    }

    private INeuralNetwork accessNeuralNetwork(int conceptId, int scenarioId, int historyPlayerId, int executionId) {
        //
        String directoryToStore = generateNetworkFileDirectoryString(conceptId, scenarioId, historyPlayerId);
        FileHandler.createDirectoryIfNotExist(directoryToStore);

        // Generate network filepath
        String fileName = generateNetworkFileName(conceptId, scenarioId, historyPlayerId, executionId);
        String filePath = directoryToStore + "/" + fileName;

        System.out.println("Network Generated: path:" + filePath);
        return new LizNeuralNetwork(generateNetworkConfiguration(), filePath);
    }


    private void assertCharacterExistsAndIsEnabledByCharacterCode(String characterCode) {
        LizCharacter character = lizCharacterRepository.findByCode(characterCode);
        if (character == null) {
            throw new LizCharacterNotFoundByCodeException(characterCode);
        }
        if (!character.isEnabled()) {
            throw new LizCharacterAssociationRestrictedException(character.getId(), character.getName());
        }
    }

    private Integer determineConceptIdFromCharacterCode(String code) {
        // Find and Validate character
        LizCharacter character = lizCharacterRepository.findByCode(code);
        if (character == null) {
            throw new LizCharacterNotFoundByUuidException(code);
        }
        if (!character.isEnabled()) {
            throw new LizCharacterAssociationRestrictedException(character.getId(), character.getName());
        }

        // Find variant
        LizVariant variant = character.getVariant();
        if (variant == null) {
            throw new LizVariantNotFoundException();
        }
        if (!variant.isEnabled()) {
            throw new LizVariantAssociationRestrictedException(variant.getId(), variant.getName());
        }

        // Find concept
        LizNeuralConcept neuralConcept = variant.getConcept();
        if (neuralConcept == null) {
            throw new LizNeuralConceptNotFoundException();
        }
        if (!neuralConcept.isEnabled()) {
            throw new LizNeuralConceptAssociationRestrictedException(neuralConcept.getId(), neuralConcept.getName());
        }
        return neuralConcept.getId();

    }

    private Integer determineLatestFinishedExecution(int conceptId) {
        LizNeuralExecution neuralExecution = lizNeuralExecutionRepository.findLatestFinished(conceptId);
        if (neuralExecution == null) {
            throw new LizNeuralConceptNoFinishedExecutionExistsException();
        }

        //        if (neuralExecution == null) {
        //            throw new LizNeuralConceptNoExecutionExistsException();
        //        }
        //        if (!LizNeuralExecution.ProcessPhase.FINISHED.equals(neuralExecution.getProcessPhase())) {
        //            throw new LizNeuralConceptNoFinishedExecutionExistsException();
        //        }
        return neuralExecution.getId();
    }


}
