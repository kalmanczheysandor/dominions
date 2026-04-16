package hu.kalmancheysandor.applications.dominions.servers.ai.liz.agent.service;


import hu.kalmancheysandor.applications.dominions.apis.ai.common.AiDecisionContext;
import hu.kalmancheysandor.applications.dominions.apis.ai.common.AiDecisionResult;

import hu.kalmancheysandor.applications.dominions.apis.ai.liz.LizAiEngine;
import hu.kalmancheysandor.applications.dominions.apis.ai.liz.LizNeuralNetwork;
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
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.entity.training.neural.LizNeuralTrainingResult;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.exception.character.LizCharacterNotFoundByUuidException;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.exception.history.scenario.LizHistoryScenarioNotFoundByUuidException;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.repository.history.LizHistoryPlayerRepository;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.repository.history.LizHistoryScenarioRepository;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.repository.training.LizCharacterRepository;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.repository.training.neural.LizNeuralExecutionRepository;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.repository.training.neural.LizNeuralTrainingResultRepository;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.service.TLizService;
import hu.kalmancheysandor.applications.dominions.apis.server.game.common.entity.game.GameScenario;
import hu.kalmancheysandor.applications.dominions.apis.server.game.common.exception.game.scenario.GameScenarioNotFoundByUuidException;
import hu.kalmancheysandor.applications.dominions.apis.server.game.common.repository.game.GameScenarioRepository;
import hu.kalmancheysandor.applications.dominions.apis.util.file.filehandler.FileHandler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
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

    @Autowired
    private GameScenarioRepository gameScenarioRepository;

    @Autowired
    private LizCharacterRepository lizCharacterRepository;





    private Integer determineConceptIdFromCharacterCode(String code) {
        // Find and Validate character
        LizCharacter character = lizCharacterRepository.findByCode(code);
        if (character == null) {
            throw new LizCharacterNotFoundByUuidException(code);
        }
        if(!character.isEnabled()) {
            throw new RuntimeException("Liz character is not enabled");
        }

        // Find variant
        LizVariant variant = character.getVariant();
        if(variant == null) {
            throw new RuntimeException("Liz variant is null");
        }
        if(!variant.isEnabled()) {
            throw new RuntimeException("Liz variant is not enabled");
        }

        // Find concept
        LizNeuralConcept neuralConcept = variant.getConcept();
        if(neuralConcept == null) {
            throw new RuntimeException("Liz neuralConcept is null");
        }
        if(!neuralConcept.isEnabled()) {
            throw new RuntimeException("Liz neuralConcept is not enabled");
        }
       return neuralConcept.getId();

    }

    private Integer determineLatestFinishedExecution(int conceptId) {
        LizNeuralExecution neuralExecution = lizNeuralExecutionRepository.findLatestFinished(conceptId);
        if(neuralExecution == null) {
            throw new RuntimeException("Liz neuralExecution is null");
        }
        if(!LizNeuralExecution.ProcessPhase.FINISHED.equals(neuralExecution.getProcessPhase())) {
            throw new RuntimeException("Liz neuralExecution process phase is not finished");
        }
        return neuralExecution.getId();
    }

    private Integer determineHistoryScenarioIdFromGameScenarioUuid(String gameScenarioUuid) {

        // Attempt to access entity in operation database
        LizHistoryScenario historyScenario = lizHistoryScenarioRepository.findByScenarioUuid(gameScenarioUuid);
        if(historyScenario == null) {
            throw new LizHistoryScenarioNotFoundByUuidException(gameScenarioUuid);
        }
        return historyScenario.getId();
    }


    public AiDecisionResponse generateResponse(AiDecisionRequest request) {
        //
        String myCharacterCode = request.getPlayerCharacterCode();
        Map<Integer, AiDecisionRequest.Player> enemyPlayers =request.getEnemyPlayers();

        //
        int conceptId = determineConceptIdFromCharacterCode(myCharacterCode);
        int latestExecutionId = determineLatestFinishedExecution(conceptId);
        int historyScenarioId = determineHistoryScenarioIdFromGameScenarioUuid(request.getScenarioUuid());



        // Initialise the decision-making engine
        INeuralAiEngine aiDecisionEngine = new LizAiEngine();

        // Register all enemy network
        for (Map.Entry<Integer, AiDecisionRequest.Player> playerEntry : enemyPlayers.entrySet()) {
            AiDecisionRequest.Player enemyPlayer = playerEntry.getValue();

            LizHistoryPlayer lizHistoryPlayer = findPlayerAndRegisterIfNotExists(enemyPlayer.getUserUuid());
            int historyPlayerId = lizHistoryPlayer.getId();

            INeuralNetwork enemyNeuralNetwork = accessNeuralNetwork(conceptId, historyScenarioId,historyPlayerId, latestExecutionId);
            aiDecisionEngine.registerEnemyNetwork(enemyPlayer.getUserUuid(), enemyNeuralNetwork);
        }

        // Make a decision
        AiDecisionContext decisionContext = convertRequestToDecisionContext(request);
        AiDecisionResult result = aiDecisionEngine.makeDecision(decisionContext);

        // Generate response
        return convertDecisionResultToResponse(result);
    }


    private INeuralNetwork  accessNeuralNetwork(int conceptId, int scenarioId, int historyPlayerId, int executionId) {
            //
            String directoryToStore = generateNetworkFileDirectoryString(conceptId, scenarioId, historyPlayerId);
            FileHandler.createDirectoryIfNotExist(directoryToStore);

            // Generate network filepath
            String fileName = generateNetworkFileName(conceptId, scenarioId, historyPlayerId, executionId);
            String filePath = directoryToStore + "/" + fileName;

            System.out.println("Network Generated: path:" + filePath);
            return new LizNeuralNetwork(generateNetworkConfiguration(), filePath);
        }



    private LizHistoryPlayer findPlayerAndRegisterIfNotExists(String userUuid) {
        // Determine current history-player and its id
        LizHistoryPlayer lizHistoryPlayer = lizHistoryPlayerRepository.findByUserUuid(userUuid);
        if (lizHistoryPlayer == null) { // Register player if it was not
            lizHistoryPlayer = lizHistoryPlayerRepository.save(new LizHistoryPlayer(userUuid));
        }
        return lizHistoryPlayer;
    }


}
