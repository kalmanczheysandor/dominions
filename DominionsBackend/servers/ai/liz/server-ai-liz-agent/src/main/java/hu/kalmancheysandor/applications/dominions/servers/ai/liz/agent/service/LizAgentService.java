package hu.kalmancheysandor.applications.dominions.servers.ai.liz.agent.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializationFeature;

import hu.kalmancheysandor.applications.dominions.apis.ai.liz.LizAiEngine;
import hu.kalmancheysandor.applications.dominions.apis.ai.liz.LizNeuralNetwork;
import hu.kalmancheysandor.applications.dominions.apis.ai.neural.INeuralAiEngine;
import hu.kalmancheysandor.applications.dominions.apis.ai.neural.INeuralNetwork;
import hu.kalmancheysandor.applications.dominions.apis.game.common.orchestration.PlayState;
import hu.kalmancheysandor.applications.dominions.apis.game.common.orchestration.player.PlayerData;
import hu.kalmancheysandor.applications.dominions.apis.game.common.representation.action.GameAction;
import hu.kalmancheysandor.applications.dominions.apis.game.common.representation.state.GameState;
import hu.kalmancheysandor.applications.dominions.apis.game.common.representation.state.exception.GeneralGameStateException;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.common.dto.AiDecisionRequest;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.common.dto.AiDecisionResponse;

import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.entity.history.LizHistory;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.entity.history.LizHistoryPlayer;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.entity.history.LizHistoryScenario;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.entity.history.LizHistorySession;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.entity.training.LizCharacter;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.entity.training.LizVariant;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.entity.training.neural.LizNeuralConcept;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.entity.training.neural.LizNeuralExecution;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.exception.character.LizCharacterNotFoundByUuidException;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.exception.history.LizHistoryPlayerNotFoundByUuidException;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.exception.history.LizHistoryScenarioNotFoundByUuidException;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.exception.history.LizHistorySessionNotFoundByUuidException;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.repository.history.LizHistoryPlayerRepository;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.repository.history.LizHistoryRepository;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.repository.history.LizHistoryScenarioRepository;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.repository.history.LizHistorySessionRepository;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.repository.training.LizCharacterRepository;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.repository.training.neural.LizNeuralExecutionRepository;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.repository.training.neural.LizNeuralTrainingResultRepository;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.service.TLizService;
import hu.kalmancheysandor.applications.dominions.apis.server.game.common.repository.game.GameScenarioRepository;
import hu.kalmancheysandor.applications.dominions.apis.util.file.filehandler.FileHandler;

import hu.kalmancheysandor.applications.dominions.servers.ai.liz.agent.etc.PlayerDecision;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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

    @Autowired
    private LizHistoryRepository lizHistoryRepository;


    @Autowired
    private LizHistorySessionRepository lizHistorySessionRepository;

//    public void maintainHistory(PlayState playState, String sessionUuid, String scenarioUuid, String scenarioName) {
//
//        // Initialisation
//        GameState gameState = playState.getGameState();
//        int turn = playState.getTurn();
//
//        Set<GameAction> intentions = new HashSet<>();
//        for (Map.Entry<Integer, PlayerData> playerEntry : playState.getPlayers().entrySet()) {
//
//            // Initialisation
//            Integer playerIndex = playerEntry.getKey();
//            PlayerData playerData = playerEntry.getValue();
//            String userUuid = playerData.getUserUuid();
//
//            // Not to store data of dead player
//            if (!gameState.getOpponent(playerIndex).isAlive()) {
//                continue;
//            }
//
//            if (!playerData.isIntentionAlreadyGiven()) {
//                throw new GeneralGameStateException("No intention is present for player! Player index:" + playerData.getIndex());
//            }
//
//            //
//            GameAction playerIntention = playerData.getIntention();
//            int reserveSize = gameState.getOpponents()[playerIndex].getReserveSize();
//            int enemiesCount = gameState.getOpponents().length - 1;
//            String playerNameCode = playerData.getName();
//
//            //
//            saveAHistory(sessionUuid, scenarioUuid, scenarioName, turn, userUuid, PlayerDecision.create(
//                            playerIndex,
//                            playerIntention.getTargetCellKey(),
//                            playerIntention.getAttackingTroopSize(),
//                            reserveSize,
//                            enemiesCount,
//                            gameState
//                    )
//            );
//        }
//
//
//    }
//
//
//    private void saveAHistory(String sessionUuid, String scenarioUuid, String scenarioName, int turn, String userUuid, PlayerDecision playerDecision) {
//
//        // Find or register local representations
//        LizHistorySession lizHistorySession = registerHistorySessionIfNotExists(sessionUuid);
//        LizHistoryPlayer lizHistoryPlayer = registerHistoryPlayerIfNotExists(userUuid);
//        LizHistoryScenario lizHistoryScenario = registerHistoryScenarioIfNotExists(scenarioUuid, scenarioName);
//
//        // Save history
//        lizHistoryRepository.save(LizHistory.builder()
//                .sessionId(lizHistorySession.getId())
//                .turn(turn)
//                .playerId(lizHistoryPlayer.getId())
//                .scenarioId(lizHistoryScenario.getId())
//                .decision(convertPlayerDecisionObjToJson(playerDecision))
//                .dateCreated(LocalDateTime.now())
//                .build()
//        );
//    }
//
//
//    private String convertPlayerDecisionObjToJson(PlayerDecision playerDecision) {
//        try {
//            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
//            return objectMapper.writeValueAsString(playerDecision);
//        } catch (JsonProcessingException e) {
//            throw new GeneralGameStateException("Error at parsing");
//        }
//    }


    public AiDecisionResponse generateResponse(AiDecisionRequest request) {

        //
        String sessionUuid = request.getSessionUuid();
        String scenarioUuid = request.getScenarioUuid();
        String scenarioName = request.getScenarioName();
        String userUuid = request.getYourUserUuid();
        String userName = request.getYourUserName();
        String myCharacterCode = request.getPlayerCharacterCode();
        Map<Integer, AiDecisionRequest.Player> enemyPlayers = request.getEnemyPlayers();

        // Register local representations if not exist
        registerHistorySessionIfNotExists(sessionUuid);
        registerHistoryPlayerIfNotExists(userUuid,userName);
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

//
//    private LizHistoryPlayer findPlayerAndRegisterIfNotExists(String userUuid) {
//        // Determine current history-player and its id
//        LizHistoryPlayer lizHistoryPlayer = lizHistoryPlayerRepository.findByUserUuid(userUuid);
//        if (lizHistoryPlayer == null) { // Register player if it was not
//            lizHistoryPlayer = lizHistoryPlayerRepository.save(new LizHistoryPlayer(userUuid));
//        }
//        return lizHistoryPlayer;
//    }


    private Integer determineConceptIdFromCharacterCode(String code) {
        // Find and Validate character
        LizCharacter character = lizCharacterRepository.findByCode(code);
        if (character == null) {
            throw new LizCharacterNotFoundByUuidException(code);
        }
        if (!character.isEnabled()) {
            throw new RuntimeException("Liz character is not enabled");
        }

        // Find variant
        LizVariant variant = character.getVariant();
        if (variant == null) {
            throw new RuntimeException("Liz variant is null");
        }
        if (!variant.isEnabled()) {
            throw new RuntimeException("Liz variant is not enabled");
        }

        // Find concept
        LizNeuralConcept neuralConcept = variant.getConcept();
        if (neuralConcept == null) {
            throw new RuntimeException("Liz neuralConcept is null");
        }
        if (!neuralConcept.isEnabled()) {
            throw new RuntimeException("Liz neuralConcept is not enabled");
        }
        return neuralConcept.getId();

    }

    private Integer determineLatestFinishedExecution(int conceptId) {
        LizNeuralExecution neuralExecution = lizNeuralExecutionRepository.findLatestFinished(conceptId);
        if (neuralExecution == null) {
            throw new RuntimeException("Liz neuralExecution is null");
        }
        if (!LizNeuralExecution.ProcessPhase.FINISHED.equals(neuralExecution.getProcessPhase())) {
            throw new RuntimeException("Liz neuralExecution process phase is not finished");
        }
        return neuralExecution.getId();
    }

//    private Integer determineHistoryScenarioIdFromGameScenarioUuid(String gameScenarioUuid) {
//
//        // Attempt to access entity in operation database
//        LizHistoryScenario historyScenario = lizHistoryScenarioRepository.findByScenarioUuid(gameScenarioUuid);
//        if (historyScenario == null) {
//            throw new LizHistoryScenarioNotFoundByUuidException(gameScenarioUuid);
//        }
//        return historyScenario.getId();
//    }










    /// /////////////////////////////
    /// /////////////////////////////
    /// /////////////////////////////

}
