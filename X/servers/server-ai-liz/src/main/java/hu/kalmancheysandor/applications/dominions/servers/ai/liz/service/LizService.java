package hu.kalmancheysandor.applications.dominions.servers.ai.liz.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import hu.kalmancheysandor.applications.dominions.apis.ai.common.AiDecisionContext;
import hu.kalmancheysandor.applications.dominions.apis.ai.common.AiDecisionResult;

import hu.kalmancheysandor.applications.dominions.apis.ai.common.TAiEngine;
import hu.kalmancheysandor.applications.dominions.apis.ai.liz.LizAiEngine;
import hu.kalmancheysandor.applications.dominions.apis.ai.liz.LizInputData;
import hu.kalmancheysandor.applications.dominions.apis.ai.liz.LizNeuralNetwork;
import hu.kalmancheysandor.applications.dominions.apis.ai.liz.LizNeuralNetworkTrainingConfiguration;
import hu.kalmancheysandor.applications.dominions.apis.ai.neural.INeuralAiEngine;
import hu.kalmancheysandor.applications.dominions.apis.ai.neural.INeuralInputData;
import hu.kalmancheysandor.applications.dominions.apis.ai.neural.INeuralNetwork;
import hu.kalmancheysandor.applications.dominions.apis.ai.neural.NeuralNetworkTrainingResult;
import hu.kalmancheysandor.applications.dominions.apis.game.common.orchestration.PlayState;
import hu.kalmancheysandor.applications.dominions.apis.game.common.orchestration.player.PlayerData;
import hu.kalmancheysandor.applications.dominions.apis.game.common.representation.action.GameAction;
import hu.kalmancheysandor.applications.dominions.apis.game.common.representation.state.GameState;
import hu.kalmancheysandor.applications.dominions.apis.game.common.representation.state.exception.GeneralGameStateException;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.common.dto.AiDecisionRequest;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.common.dto.AiDecisionResponse;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.common.dto.queue.AiHistoryQueueItem;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.common.service.TAiService;

import hu.kalmancheysandor.applications.dominions.apis.server.common.component.config.ApplicationConfig;
import hu.kalmancheysandor.applications.dominions.apis.util.file.filehandler.FileHandler;
import hu.kalmancheysandor.applications.dominions.apis.util.uuid.UUIDGenerator;

import hu.kalmancheysandor.applications.dominions.servers.ai.liz.entity.history.LizHistory;
import hu.kalmancheysandor.applications.dominions.servers.ai.liz.entity.history.LizHistoryPlayer;
import hu.kalmancheysandor.applications.dominions.servers.ai.liz.entity.history.LizHistorySession;
import hu.kalmancheysandor.applications.dominions.servers.ai.liz.entity.training.LizTrainingSnapshot;
import hu.kalmancheysandor.applications.dominions.servers.ai.liz.etc.PlayerDecision;
import hu.kalmancheysandor.applications.dominions.servers.ai.liz.entity.training.LizTraining;
import hu.kalmancheysandor.applications.dominions.servers.ai.liz.entity.training.LizTrainingResult;
import hu.kalmancheysandor.applications.dominions.servers.ai.liz.entity.training.LizTrainingResultLatest;
import hu.kalmancheysandor.applications.dominions.servers.ai.liz.repository.history.LizHistoryPlayerRepository;
import hu.kalmancheysandor.applications.dominions.servers.ai.liz.repository.history.LizHistoryRepository;
import hu.kalmancheysandor.applications.dominions.servers.ai.liz.repository.history.LizHistorySessionRepository;
import hu.kalmancheysandor.applications.dominions.servers.ai.liz.repository.training.LizTrainingResultLatestRepository;
import hu.kalmancheysandor.applications.dominions.servers.ai.liz.repository.training.LizTrainingResultRepository;
import hu.kalmancheysandor.applications.dominions.servers.ai.liz.repository.training.LizTrainingRepository;

import hu.kalmancheysandor.applications.dominions.servers.ai.liz.repository.training.LizTrainingSnapshotRepository;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@Transactional
public class LizService extends TAiService {


    @Autowired
    private LizHistoryRepository lizHistoryRepository;

    @Autowired
    private LizHistoryPlayerRepository lizHistoryPlayerRepository;

    @Autowired
    private LizHistorySessionRepository lizHistorySessionRepository;

    @Autowired
    private LizTrainingRepository lizTrainingRepository;

    @Autowired
    private LizTrainingResultRepository lizTrainingResultRepository;

    @Autowired
    private LizTrainingSnapshotRepository lizTrainingSnapshotRepository;

    @Autowired
    private LizTrainingResultLatestRepository lizTrainingResultLatestRepository;

    @Autowired
    private UUIDGenerator uuidGenerator;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ApplicationConfig applicationConfig;

    public AiDecisionResponse generateResponse(AiDecisionRequest request) {
        int scenarioId = request.getScenarioId();

        // Initialise the decision-making engine
        INeuralAiEngine aiDecisionEngine = new LizAiEngine();

        // Register all enemy network
        for (Map.Entry<Integer, AiDecisionRequest.Player> playerEntry : request.getPlayers().entrySet()) {
            AiDecisionRequest.Player player = playerEntry.getValue();

            INeuralNetwork neuralNetwork = generateNetwork(player.getUserUuid(), scenarioId);
            aiDecisionEngine.registerEnemyNetwork(player.getUserUuid(), neuralNetwork);
        }

        // Make a decision
        AiDecisionContext context = convertRequestToDecisionContext(request);
        AiDecisionResult result = aiDecisionEngine.makeDecision(context);

        // Generate response
        return convertDecisionResultToResponse(result);
    }

    public void deleteScenario(int scenarioId) {

        // Delete all related records
        lizHistoryRepository.deleteAllWhereScenarioId(scenarioId);
        lizTrainingSnapshotRepository.deleteAllWhereScenarioId(scenarioId);
        lizTrainingResultRepository.deleteAllWhereScenarioId(scenarioId);
        lizTrainingRepository.deleteAllWhereScenarioId(scenarioId);

        // Delete related folders and files
        String folderToDelete = generateScenarioDirectory(scenarioId);
        FileHandler.deleteDirectoryRecursivelyIfExists(folderToDelete);
    }


    public void train() {

        // Create a new training record in db
        LizTraining trainingToSave = new LizTraining();
        trainingToSave.setName("Liz Training " + LocalDateTime.now().toString());
        trainingToSave.setScenarioId(1);// TODO: nem lehet fix
        trainingToSave.setDateCreated(LocalDateTime.now());
        LizTraining trainingCreated = uuidGenerator.saveWithRetry(lizTrainingRepository, trainingToSave);

        // Set training configuration
        LizNeuralNetworkTrainingConfiguration trainingConfiguration = LizNeuralNetworkTrainingConfiguration.builder()
                .maxIterationsPerTurn(10)
                .maxTurn(500)
                .learningRate(0.1)
                .build();

        // Minden jatekos bejarasa
        List<LizHistoryPlayer> players = lizHistoryPlayerRepository.findAll();
        for (LizHistoryPlayer player : players) {
            trainAPlayer(trainingCreated.getScenarioId(), player.getId(), trainingCreated.getId(), trainingConfiguration);
        }
    }

    public void check() {
//        Map<String, List<History>> groups = splitHistoryIntoGroups(lizHistoryRepository.findAll());
//        for (Map.Entry<String, List<History>> groupEntry : groups.entrySet()) {
//            engine.testIt(groupEntry.getKey(), convertHistoryListToNeuralTrainingDataList(groupEntry.getValue()));
//        }
    }


    private void evaluateANetwork() {

    }


    private LizHistoryPlayer findPlayerAndRegisterIfNotExists(String userUuid) {
        // Determine current history-player and its id
        LizHistoryPlayer lizHistoryPlayer = lizHistoryPlayerRepository.findByUserUuid(userUuid);
        if (lizHistoryPlayer == null) { // Register player if it was not
            lizHistoryPlayer = lizHistoryPlayerRepository.save(new LizHistoryPlayer(userUuid));
        }
        return lizHistoryPlayer;
    }

    private static LizNeuralNetwork.Configuration generateNetworkConfiguration() {
        return LizNeuralNetwork.Configuration.builder()
                .enemiesCountMin(TAiEngine.ENEMIES_COUNT_MIN)
                .enemiesCountMax(TAiEngine.ENEMIES_COUNT_MAX)
                .ownerRankMinIndex(TAiEngine.OWNER_RANKS_INDEX_MIN)
                .ownerRankMaxIndex(TAiEngine.OWNER_RANKS_INDEX_MAX)
                .defendersMinCount(TAiEngine.DEFENDERS_COUNT_MIN)
                .defendersMaxCount(TAiEngine.DEFENDERS_COUNT_MAX)
                .attackPowerMin(TAiEngine.RESERVE_SIZE_MIN)
                .attackPowerMax(TAiEngine.RESERVE_SIZE_MAX)
                .cellCount(42)// TODO ez nem lehet fix
                .build();
    }

    private INeuralNetwork generateNetwork(String userUuid, int scenarioId) {
        //
        LizHistoryPlayer lizHistoryPlayer = findPlayerAndRegisterIfNotExists(userUuid);
        int historyPlayerId = lizHistoryPlayer.getId();

        // Determine latest training,if no one exists than a blank is generated
        int latestTrainingId = 0;
        LizTrainingResultLatest latestTrainingLog = lizTrainingResultLatestRepository.findLatest(scenarioId, historyPlayerId);
        if (latestTrainingLog != null) {
            latestTrainingId = latestTrainingLog.getTrainingId();
        }

        //
        String directoryToStore = generateNetworkFileDirectory(scenarioId, historyPlayerId);
        FileHandler.createDirectoryIfNotExist(directoryToStore);

        // Generate network filepath
        String fileName = generateNetworkFileName(scenarioId, historyPlayerId, latestTrainingId);
        String filePath = directoryToStore + "/" + fileName;

        System.out.println("Network Generated: <scenarioId: " + scenarioId + ", historyPlayerId: " + historyPlayerId + ", latestTrainingId: " + latestTrainingId + ">");
        return new LizNeuralNetwork(generateNetworkConfiguration(), filePath);
    }

    private String generateScenarioDirectory(int scenarioId) {
        return applicationConfig.getLizServer().getNeuralNetwork().getBaseFolder()+"/trainings/scenario-" + scenarioId;
    }
    private String generateNetworkFileDirectory(int scenarioId, int playerId) {
        return generateScenarioDirectory(scenarioId) + "/player-" + playerId;
    }

    private static String generateNetworkFileName(int scenarioId, int playerId, int trainingId) {
        return "LizAi_Scenario" + scenarioId + "_Player" + playerId + "_Training" + trainingId + ".nnet";
    }

    private void trainAPlayer(int scenarioId, int playerId, int trainingId, LizNeuralNetworkTrainingConfiguration trainingConfiguration) {
        try {

            // Prepare destination folder
            String directoryToStore = generateNetworkFileDirectory(scenarioId, playerId);
            FileHandler.createDirectoryIfNotExist(directoryToStore);

            // Generate network filepath
            String fileName = generateNetworkFileName(scenarioId, playerId, trainingId);
            String filePath = directoryToStore + "/" + fileName;

            // Provide training and testing records
            List<LizHistory> historyList = lizHistoryRepository.listAllAtPlayerIdAndScenarioId(playerId, scenarioId);
            List<INeuralInputData> trainingDataList = new ArrayList<>();
            List<INeuralInputData> testingDataList = new ArrayList<>();
            int itemCount = 0;
            for (LizHistory history : historyList) {
                // Create decision object

                INeuralInputData inputData = convertPlayerDecisionToNeuralTrainingData(objectMapper.readValue(history.getDecision(), PlayerDecision.class));
                trainingDataList.add(inputData);
                testingDataList.add(inputData);
//                if (itemCount % 2 == 0) {
//                    trainingDataList.add(inputData);
//                } else {
//                    testingDataList.add(inputData);
//                }

                itemCount++;
            }

            //
            System.out.println("LIZ AI Start training...");
            System.out.println("FilePath code: " + filePath);
            System.out.println("Training data list:" + trainingDataList.size());
            System.out.println("Test data list:" + testingDataList.size());

            // Execution
            INeuralNetwork nuralNetwork = new LizNeuralNetwork(generateNetworkConfiguration(), filePath);
            NeuralNetworkTrainingResult trainingResult = null;
            double precisionRate = 0.0;
            if (!trainingDataList.isEmpty()) {
                trainingResult = nuralNetwork.train(trainingDataList, trainingConfiguration, testingDataList);
                precisionRate = trainingResult.getPrecisionRate();

                saveTrainingSnapshots(scenarioId,playerId,trainingId,trainingResult.getSnapshotsList());
            }
            nuralNetwork.save();

            // Save training result
            LizTrainingResult trainingLogToSave = new LizTrainingResult();
            trainingLogToSave.setTrainingId(trainingId);
            trainingLogToSave.setScenarioId(scenarioId);
            trainingLogToSave.setPlayerId(playerId);
            trainingLogToSave.setPrecisionRate(BigDecimal.valueOf(precisionRate));
            trainingLogToSave.setDateCreated(LocalDateTime.now());
            lizTrainingResultRepository.save(trainingLogToSave);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    private void saveTrainingSnapshots(int scenarioId, int playerId, int trainingId, List<NeuralNetworkTrainingResult.Snapshot> trainingSnapshotsList) {

        // Save training snapshots
        for (NeuralNetworkTrainingResult.Snapshot snapshot : trainingSnapshotsList) {
            lizTrainingSnapshotRepository.save(LizTrainingSnapshot.builder()
                    .scenarioId(scenarioId)
                    .trainingId(trainingId)
                    .playerId(playerId)
                    .turn(snapshot.getTurn())
                    .iterationCount(snapshot.getIterationCount())
                    .precisionRate(BigDecimal.valueOf(snapshot.getEvaluationResult().getPrecisionRate()))
                    .dateCreated(LocalDateTime.now())
                    .build()
            );
        }
    }




    private INeuralInputData convertPlayerDecisionToNeuralTrainingData(PlayerDecision decisionObj) {
        System.out.println("-------convertHistoryToTrainingData-----");
        try {


            int[] cellOwners = new int[decisionObj.getFields().size()];
            int[] cellDefendersSize = new int[decisionObj.getFields().size()];
            for (Map.Entry<Integer, PlayerDecision.Field> fieldEntry : decisionObj.getFields().entrySet()) {
                int fieldIndex = fieldEntry.getKey();
                PlayerDecision.Field fieldObj = fieldEntry.getValue();

                int playerKey = fieldObj.rankIndex;

                cellOwners[fieldIndex] = playerKey;
                cellDefendersSize[fieldIndex] = fieldObj.getDefenders();
            }

            //Output
            LizInputData trainingData = new LizInputData();
            trainingData.setAttackPower(decisionObj.getReserveSize());

            trainingData.setCellOwnerRanks(cellOwners);
            trainingData.setCellDefendersSize(cellDefendersSize);

            trainingData.setChosenTarget(decisionObj.getDecision().getTarget());

            return trainingData;
        } catch (Exception e) {
            System.out.println(">>>>>>>>CONVERSION ERROR");

            //e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    private String convertPlayerDecisionObjToJson(PlayerDecision playerDecision) {
        try {
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
            return objectMapper.writeValueAsString(playerDecision);
        } catch (JsonProcessingException e) {
            throw new GeneralGameStateException("Error at parsing");
        }
    }


    /// ///////////////////////////////////////////////////////////////////////////////////////////////////////
    ///  QUEUE METHODS  //////////////////////////////////////////////////////////////////////////////////////
    /// ///////////////////////////////////////////////////////////////////////////////////////////////////////
    public void queueHistoryRead(AiHistoryQueueItem queueItem) {

        // Determine current session and its id
        LizHistorySession lizHistorySession = lizHistorySessionRepository.findBySessionUuid(queueItem.getSessionUuid());
        if (lizHistorySession == null) {    // Register game-session if it was not
            lizHistorySession = lizHistorySessionRepository.save(new LizHistorySession(queueItem.getSessionUuid()));
        }
        int historySessionId = lizHistorySession.getId();


        // Initialise
        PlayState playState = queueItem.getPlayState();
        int turn = playState.getTurn();
        Map<Integer, PlayerData> players = playState.getPlayers();
        GameState gameState = playState.getGameState();

        //
        for (Map.Entry<Integer, PlayerData> playerEntry : players.entrySet()) {
            // ??
            Integer playerIndex = playerEntry.getKey();
            PlayerData playerData = playerEntry.getValue();

            // Ignore dead players
            if (!gameState.getOpponent(playerIndex).isAlive()) {
                continue;
            }

            // Check intention existence
            if (!playerData.isIntentionAlreadyGiven()) {
                throw new GeneralGameStateException("No intention is present for player! Player index:" + playerData.getIndex());
            }

            // Determine current history-player and its id
            LizHistoryPlayer lizHistoryPlayer = findPlayerAndRegisterIfNotExists(playerData.getUserUuid());
            int historyPlayerId = lizHistoryPlayer.getId();

            // Generate decision object
            PlayerDecision playerDecisionObj = generatePlayerDecision(playerIndex, playerData, gameState);

            // Save history
            LizHistory lizHistory = new LizHistory();
            lizHistory.setSessionId(historySessionId);
            lizHistory.setTurn(turn);
            lizHistory.setPlayerId(historyPlayerId);
            lizHistory.setScenarioId(queueItem.getScenarioId());
            lizHistory.setDecision(convertPlayerDecisionObjToJson(playerDecisionObj));
            lizHistory.setDateCreated(LocalDateTime.now());
            lizHistoryRepository.save(lizHistory);
        }
    }

    private PlayerDecision generatePlayerDecision(int playerIndex, @NotNull PlayerData playerData, @NotNull GameState gameState) {
        GameAction playerIntention = playerData.getIntention();
        int reserveSize = gameState.getOpponents()[playerIndex].getReserveSize();
        int enemiesCount = gameState.getOpponents().length - 1;

        return PlayerDecision.create(playerIndex, playerIntention.getTargetCellKey(), playerIntention.getAttackingTroopSize(), reserveSize, enemiesCount, gameState);
    }


}
