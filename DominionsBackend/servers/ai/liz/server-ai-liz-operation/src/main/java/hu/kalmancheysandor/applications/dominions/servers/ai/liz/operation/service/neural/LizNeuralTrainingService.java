package hu.kalmancheysandor.applications.dominions.servers.ai.liz.operation.service.neural;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.kalmancheysandor.applications.dominions.apis.ai.engine.liz.*;
import hu.kalmancheysandor.applications.dominions.apis.ai.neural.network.INeuralNetwork;
import hu.kalmancheysandor.applications.dominions.apis.ai.neural.NeuralNetworkTrainingResult;
import hu.kalmancheysandor.applications.dominions.apis.ai.neural.NeuralNetworkTrainingSnapshot;
import hu.kalmancheysandor.applications.dominions.apis.ai.neural.network.INeuralNetworkTrainingItem;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.entity.history.LizHistory;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.entity.training.neural.LizNeuralExecution;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.entity.training.neural.LizNeuralTrainingResult;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.entity.training.neural.LizNeuralTrainingSnapshot;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.entity.training.neural.LizNeuralTrainingTask;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.repository.history.LizHistoryRepository;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.repository.training.neural.*;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.service.TLizService;
import hu.kalmancheysandor.applications.dominions.apis.util.file.filehandler.FileHandler;
import hu.kalmancheysandor.applications.dominions.servers.ai.liz.operation.etc.PlayerDecision;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Slf4j
@Service
@Transactional
public class LizNeuralTrainingService extends TLizService {

    @Autowired
    private LizHistoryRepository lizHistoryRepository;

    @Autowired
    private LizNeuralExecutionRepository lizNeuralExecutionRepository;
    @Autowired
    private LizNeuralTrainingResultRepository lizNeuralTrainingResultRepository;

    @Autowired
    private LizNeuralTrainingSnapshotRepository lizNeuralTrainingSnapshotRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Transactional(propagation = Propagation.NEVER)
    public void trainAPlayer(LizNeuralTrainingTask trainingTask, LizNeuralNetworkTrainingConfiguration trainingConfiguration) {
        int conceptId = trainingTask.getConceptId();
        int executionId = trainingTask.getExecutionId();
        int scenarioId = trainingTask.getScenarioId();
        int playerId = trainingTask.getPlayerId();

        try {

            // Prepare destination folder
            String directoryToStore = generateNetworkFileDirectoryStringByTrainingTask(trainingTask);
            FileHandler.createDirectoryIfNotExist(directoryToStore);

            // Generate network filepath
            String fileName = generateNetworkFileNameByTrainingTask(trainingTask);
            String filePath = directoryToStore + "/" + fileName;

            // Collecting input data for training and testing
            Map<String, List<INeuralNetworkTrainingItem>> x = collectDataForTrainingAndTesting(scenarioId, playerId);
            List<INeuralNetworkTrainingItem> trainingItemList = x.getOrDefault("trainingData", List.of());
            List<INeuralNetworkTrainingItem> testingItemList = x.getOrDefault("testingData", List.of());

            //
            System.out.println("LIZ AI Start training...");
            System.out.println("FilePath code: " + filePath);
            System.out.println("Training data list:" + trainingItemList.size());
            System.out.println("Test data list:" + testingItemList.size());

            // Initialisation of neural engine
            INeuralNetwork neuralNetwork = new LizNeuralNetwork(generateNetworkConfiguration(), filePath);
            neuralNetwork.setTrainingSnapshotListener(snapshot -> {
                transactionTemplate.executeWithoutResult(status -> {
                    saveTrainingSnapshot(conceptId, executionId, scenarioId, playerId, snapshot);
                });
            });
            neuralNetwork.setTrainingInterruptListener(() -> {
                Boolean isInterrupt = transactionTemplate.execute(status -> {
                    LizNeuralExecution executionToCheck = lizNeuralExecutionRepository.findById(executionId);
                    if (executionToCheck == null) { // INTERRUPT:OK - due: no record found
                        System.out.println("INTERRUPT:OK - due: no record found");
                        return true;
                    }
                    if (!LizNeuralExecution.ProcessPhase.RUNNING.equals(executionToCheck.getProcessPhase())) { // INTERRUPT:OK - due: RUNNING PHASE
                        System.out.println("INTERRUPT:OK - due: " + executionToCheck.getProcessPhase());
                        return true;
                    }
                    return false;

                });

                return Boolean.TRUE.equals(isInterrupt);
            });

            // Execution
            NeuralNetworkTrainingResult neuralTrainingResult = null;
            boolean isTrainingStarted = false;
            if (!trainingItemList.isEmpty()) {
                isTrainingStarted = true;
                neuralTrainingResult = neuralNetwork.train(
                        trainingItemList,
                        trainingConfiguration,
                        testingItemList
                );
            }

            //
            if (!isTrainingStarted) {   // it occurs when no training data exists. It counts to be a valid case, so an initial network has to be saved. Without this solution the previously never played players would cause a never ending problem-cycle
                neuralNetwork.save();
                saveTrainingResultInSeparateTransaction(conceptId, executionId, scenarioId, playerId, neuralTrainingResult);
            } else if (isTrainingStarted && NeuralNetworkTrainingResult.Status.DONE.equals(neuralTrainingResult.getStatus())) { // no cancel occurred, so training is fully done
                neuralNetwork.save();
                saveTrainingResultInSeparateTransaction(conceptId, executionId, scenarioId, playerId, neuralTrainingResult);
            } else if (isTrainingStarted && NeuralNetworkTrainingResult.Status.CANCELED.equals(neuralTrainingResult.getStatus())) { // When cancel occurred
                deleteAllTrainingSnapshotInSeparateTransaction(conceptId, executionId, scenarioId, playerId);
            } else {
                throw new RuntimeException("Unsupported case is found");
            }


        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private Map<String, List<INeuralNetworkTrainingItem>> collectDataForTrainingAndTesting(int scenarioId, int playerId) throws JsonProcessingException {

        // Provide training and testing records
        List<LizHistory> historyList = lizHistoryRepository.listAllAtPlayerIdAndScenarioId(playerId, scenarioId);
        List<INeuralNetworkTrainingItem> trainingItemList = new ArrayList<>();
        List<INeuralNetworkTrainingItem> testingItemList = new ArrayList<>();

        int itemCount = 0;
        for (LizHistory history : historyList) {
            //
            PlayerDecision playerDecisionObj = objectMapper.readValue(history.getDecision(), PlayerDecision.class);

            // Create decision object
            INeuralNetworkTrainingItem item = convertPlayerDecisionToNeuralNetworkTrainingItem(playerDecisionObj);
            if (itemCount % 2 == 0) {
                trainingItemList.add(item);
            } else {
                testingItemList.add(item);
            }
            itemCount++;
        }
        return Map.of("trainingData", trainingItemList, "testingData", testingItemList);
    }
//
//    private Map<String, List<INeuralInputData>> collectDataForTrainingAndTesting_old(int scenarioId, int playerId) throws JsonProcessingException {
//
//        // Provide training and testing records
//        List<LizHistory> historyList = lizHistoryRepository.listAllAtPlayerIdAndScenarioId(playerId, scenarioId);
//        List<INeuralInputData> trainingDataList = new ArrayList<>();
//        List<INeuralInputData> testingDataList = new ArrayList<>();
//        int itemCount = 0;
//        for (LizHistory history : historyList) {
//            // Create decision object
//            INeuralInputData inputData = convertPlayerDecisionToNeuralNetworkTrainingItem(objectMapper.readValue(history.getDecision(), PlayerDecision.class));

    /// /            if (itemCount % 2 == 0) {
    /// /                trainingDataList.add(inputData);
    /// /            } else {
    /// /                testingDataList.add(inputData);
    /// /            }
//            trainingDataList.add(inputData);
//            testingDataList.add(inputData);
//            itemCount++;
//        }
//        return Map.of("trainingData", trainingDataList, "testingData", testingDataList);
//    }
    private INeuralNetworkTrainingItem convertPlayerDecisionToNeuralNetworkTrainingItem(PlayerDecision decisionObj) {
        System.out.println("-------convertPlayerDecisionToNeuralNetworkTrainingItem-----");
        try {

            //
            int cellCount = decisionObj.getBoardFields().size();
            int playerCount = decisionObj.getBoardPlayers().size();
            int targetedCell = decisionObj.getDecision().getTarget();   // increases from -1
            int appliedOperatorIndex = targetedCell + 1;// increases from 0

            //
            int[] cellOccupierIndex = new int[cellCount];
            int[] cellGarrisonSize = new int[cellCount];
            int[] playerTerritorySize = new int[playerCount];
            int[] playerReserveSize = new int[playerCount];
            boolean[] playerIsAlive = new boolean[playerCount];
            double[] operatorProbability = new double[cellCount + 1];

            //
            for (PlayerDecision.BoardField boardField : decisionObj.getBoardFields()) {
                cellOccupierIndex[boardField.getIndex()] = boardField.getOccupierIndex();
                cellGarrisonSize[boardField.getIndex()] = boardField.getGarrisonSize();
            }

            //
            for (PlayerDecision.BoardPlayer boardPlayer : decisionObj.getBoardPlayers()) {
                playerTerritorySize[boardPlayer.getIndex() - 1] = boardPlayer.getTerritorySize();
                playerReserveSize[boardPlayer.getIndex() - 1] = boardPlayer.getReserveSize();
                playerIsAlive[boardPlayer.getIndex() - 1] = boardPlayer.isAlive();
            }

            //
            if (appliedOperatorIndex >= operatorProbability.length) {
                throw new RuntimeException("appliedOperatorIndex is out of bounds: " + appliedOperatorIndex + " >= " + operatorProbability.length);
            }
            operatorProbability[appliedOperatorIndex] = 1.0; // The operator that was applied is set to 1.0, while the others remain at 0.0

            return new LizNeuralNetworkTrainingItem(
                    LizNeuralNetworkModelInput.builder()
                            .cellOccupierIndex(cellOccupierIndex)
                            .cellGarrisonSize(cellGarrisonSize)
                            .playerTerritorySize(playerTerritorySize)
                            .playerReserveSize(playerReserveSize)
                            .playerIsAlive(playerIsAlive)
                            .build(),
                    LizNeuralNetworkModelOutput.builder()
                            .operatorProbability(operatorProbability)
                            .build()
            );
        } catch (Exception e) {
            System.out.println(">>>>>>>>CONVERSION ERROR");

            //e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    private void saveTrainingSnapshot(int conceptId, int executionId, int scenarioId, int playerId, NeuralNetworkTrainingSnapshot snapshot) {
        lizNeuralTrainingSnapshotRepository.save(LizNeuralTrainingSnapshot.builder()
                .conceptId(conceptId)
                .executionId(executionId)
                .scenarioId(scenarioId)
                .playerId(playerId)
                .turn(snapshot.getTurn())
                .iterationCount(snapshot.getIterationCount())
                .precisionRate(BigDecimal.valueOf(snapshot.getEvaluationResult().getPrecisionRate()))
                .dateCreated(LocalDateTime.now())
                .build()
        );
    }

    private void deleteAllTrainingSnapshotInSeparateTransaction(int conceptId, int executionId, int scenarioId, int playerId) {
        transactionTemplate.executeWithoutResult(status -> {
            lizNeuralTrainingSnapshotRepository.deleteAllWhereConceptIdAndExecutionIdAndScenarioIdAndPlayerId(
                    conceptId,
                    executionId,
                    scenarioId,
                    playerId
            );
        });
    }

    private void saveTrainingResultInSeparateTransaction(int conceptId, int executionId, int scenarioId, int playerId, NeuralNetworkTrainingResult neuralNetworkTrainingResult) {

        transactionTemplate.executeWithoutResult(status -> {
            double precisionRate = 0.0;
            int turnCount = 0;
            int turnBest = 0;

            if (neuralNetworkTrainingResult != null) {
                precisionRate = neuralNetworkTrainingResult.getPrecisionRate();
                turnCount = neuralNetworkTrainingResult.getTurnCount();
                turnBest = neuralNetworkTrainingResult.getTurnBest();
            }
            lizNeuralTrainingResultRepository.save(LizNeuralTrainingResult.builder()
                    .conceptId(conceptId)
                    .executionId(executionId)
                    .scenarioId(scenarioId)
                    .playerId(playerId)
                    .precisionRate(BigDecimal.valueOf(precisionRate))
                    .turnCount(turnCount)
                    .turnBest(turnBest)
                    .dateCreated(LocalDateTime.now())
                    .build()
            );
        });
    }


//    private void saveTrainingSnapshots(int conceptId, int executionId, int scenarioId, int playerId, List<NeuralNetworkTrainingResult.Snapshot> trainingSnapshotsList) {
//
//        // Save training snapshots
//        for (NeuralNetworkTrainingResult.Snapshot snapshot : trainingSnapshotsList) {
//            lizNeuralTrainingSnapshotRepository.save(LizNeuralTrainingSnapshot.builder()
//                    .conceptId(conceptId)
//                    .executionId(executionId)
//                    .scenarioId(scenarioId)
//                    .playerId(playerId)
//                    .turn(snapshot.getTurn())
//                    .iterationCount(snapshot.getIterationCount())
//                    .precisionRate(BigDecimal.valueOf(snapshot.getEvaluationResult().getPrecisionRate()))
//                    .dateCreated(LocalDateTime.now())
//                    .build()
//            );
//        }
//    }

}
