package hu.kalmancheysandor.applications.dominions.servers.ai.liz.operation.service.neural;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.kalmancheysandor.applications.dominions.apis.ai.liz.LizInputData;
import hu.kalmancheysandor.applications.dominions.apis.ai.liz.LizNeuralNetwork;
import hu.kalmancheysandor.applications.dominions.apis.ai.liz.LizNeuralNetworkTrainingConfiguration;
import hu.kalmancheysandor.applications.dominions.apis.ai.neural.INeuralInputData;
import hu.kalmancheysandor.applications.dominions.apis.ai.neural.INeuralNetwork;
import hu.kalmancheysandor.applications.dominions.apis.ai.neural.NeuralNetworkTrainingResult;
import hu.kalmancheysandor.applications.dominions.apis.ai.neural.NeuralNetworkTrainingSnapshot;
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
            String directoryToStore = generateNetworkFileDirectoryString(trainingTask);
            FileHandler.createDirectoryIfNotExist(directoryToStore);

            // Generate network filepath
            String fileName = generateNetworkFileName(trainingTask);
            String filePath = directoryToStore + "/" + fileName;

            // Collecting input data for training and testing
            Map<String, List<INeuralInputData>> x = collectDataForTrainingAndTesting(scenarioId, playerId);
            List<INeuralInputData> trainingDataList = x.getOrDefault("trainingData", List.of());
            List<INeuralInputData> testingDataList = x.getOrDefault("testingData", List.of());

            //
            System.out.println("LIZ AI Start training...");
            System.out.println("FilePath code: " + filePath);
            System.out.println("Training data list:" + trainingDataList.size());
            System.out.println("Test data list:" + testingDataList.size());

            // Initialisation of neural engine
            INeuralNetwork neuralNetwork = new LizNeuralNetwork(generateNetworkConfiguration(), filePath);
            neuralNetwork.setSnapshotListener(snapshot -> {
                transactionTemplate.executeWithoutResult(status -> {
                    saveTrainingSnapshot(conceptId, executionId, scenarioId, playerId, snapshot);
                });
            });
            neuralNetwork.setInterruptListener(() -> {
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
            if (!trainingDataList.isEmpty()) {
                isTrainingStarted = true;
                neuralTrainingResult = neuralNetwork.train(trainingDataList, trainingConfiguration, testingDataList);
            }

            //
            if (!isTrainingStarted) {   // it occurs when no training data exists. It counts to be a valid case, so an initial network has to be saved. Without this solution the previously never played players would cause a never ending problem-cycle
                neuralNetwork.save();
                saveTrainingResultInSeparateTransaction(conceptId, executionId, scenarioId, playerId, neuralTrainingResult);
            }
            else if (isTrainingStarted && NeuralNetworkTrainingResult.Status.DONE.equals(neuralTrainingResult.getStatus())) { // no cancel occurred, so training is fully done
                neuralNetwork.save();
                saveTrainingResultInSeparateTransaction(conceptId, executionId, scenarioId, playerId, neuralTrainingResult);
            } else if (isTrainingStarted && NeuralNetworkTrainingResult.Status.CANCELED.equals(neuralTrainingResult.getStatus())) { // When cancel occurred
                deleteAllTrainingSnapshotInSeparateTransaction(conceptId, executionId, scenarioId, playerId);
            }
            else {
                throw new RuntimeException("Unsupported case is found");
            }


        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }



    private Map<String, List<INeuralInputData>> collectDataForTrainingAndTesting(int scenarioId, int playerId) throws JsonProcessingException {

        // Provide training and testing records
        List<LizHistory> historyList = lizHistoryRepository.listAllAtPlayerIdAndScenarioId(playerId, scenarioId);
        List<INeuralInputData> trainingDataList = new ArrayList<>();
        List<INeuralInputData> testingDataList = new ArrayList<>();
        int itemCount = 0;
        for (LizHistory history : historyList) {
            // Create decision object
            INeuralInputData inputData = convertPlayerDecisionToNeuralTrainingData(objectMapper.readValue(history.getDecision(), PlayerDecision.class));
//            if (itemCount % 2 == 0) {
//                trainingDataList.add(inputData);
//            } else {
//                testingDataList.add(inputData);
//            }
            trainingDataList.add(inputData);
            testingDataList.add(inputData);
            itemCount++;
        }
        return Map.of("trainingData", trainingDataList, "testingData", testingDataList);
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

    private void saveTrainingResultInSeparateTransaction(int conceptId, int executionId, int scenarioId, int playerId,NeuralNetworkTrainingResult neuralNetworkTrainingResult) {

        transactionTemplate.executeWithoutResult(status -> {
            double precisionRate = 0.0;
            int turnCount = 0;
            int turnBest=0;

            if(neuralNetworkTrainingResult!=null) {
                precisionRate =  neuralNetworkTrainingResult.getPrecisionRate();
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
