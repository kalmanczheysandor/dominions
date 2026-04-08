package hu.kalmancheysandor.applications.dominions.servers.ai.helga.agent.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import hu.kalmancheysandor.applications.dominions.apis.ai.common.AiDecisionContext;
import hu.kalmancheysandor.applications.dominions.apis.ai.common.AiDecisionResult;
import hu.kalmancheysandor.applications.dominions.apis.ai.helga.HelgaInputData;
import hu.kalmancheysandor.applications.dominions.apis.ai.neural.INeuralAiEngine;
import hu.kalmancheysandor.applications.dominions.apis.ai.neural.INeuralInputData;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.common.dto.AiDecisionRequest;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.common.dto.AiDecisionResponse;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.common.dto.queue.AiHistoryQueueItem;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.common.service.TAiService;
import hu.kalmancheysandor.applications.dominions.apis.server.game.common.entity.history.History;
import hu.kalmancheysandor.applications.dominions.apis.server.game.common.repository.history.HistoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class HelgaAgentService extends TAiService {
    @Autowired
    private INeuralAiEngine engine;

    @Autowired
    private HistoryRepository historyRepository;

    @Autowired
    private ObjectMapper objectMapper;

    public AiDecisionResponse generateResponse(AiDecisionRequest request) {
        AiDecisionContext context = convertRequestToDecisionContext(request);
        AiDecisionResult result = engine.makeDecision(context);

        // Generate response
        return convertDecisionResultToResponse(result);
    }

    public void train() {
        System.out.println("---------TRAIN-----------");
        Map<String, List<History>> groups = splitHistoryIntoGroups(historyRepository.findAll());

        System.out.println("groups size: " + groups.size());

        System.out.println("---------Loop-----------");
        for (Map.Entry<String, List<History>> groupEntry : groups.entrySet()) {
            System.out.println("---------groupEntry-----------");
            System.out.println("Size:" + groupEntry.getValue().size());
            engine.trainIt(groupEntry.getKey(), convertHistoryListToNeuralTrainingDataList(groupEntry.getValue()));
        }
    }

    public void check() {
        Map<String, List<History>> groups = splitHistoryIntoGroups(historyRepository.findAll());
        for (Map.Entry<String, List<History>> groupEntry : groups.entrySet()) {
            engine.testIt(groupEntry.getKey(), convertHistoryListToNeuralTrainingDataList(groupEntry.getValue()));
        }
    }

    private Map<String, List<History>> splitHistoryIntoGroups(List<History> historyList) {
        System.out.println("--------splitHistoryIntoGroups--------");
        Map<String, List<History>> groups = new HashMap<>();
        String userUUid;
        for (History history : historyList) {
            userUUid = history.getUserUuid();

            if (!groups.containsKey(userUUid)) {
                groups.put(userUUid, new ArrayList<>());
            }

            groups.get(userUUid).add(history);
        }
        return groups;
    }

    private List<INeuralInputData> convertHistoryListToNeuralTrainingDataList(List<History> historyList) {
        System.out.println("-------convertHistoryListToTrainingDataList-----");
        System.out.println("historyList size:" + historyList.size());
        List<INeuralInputData> trainingDataList = new ArrayList<>();
        for (History history : historyList) {
            trainingDataList.add(convertHistoryToNeuralTrainingData(history));
        }
        return trainingDataList;
    }

    private INeuralInputData convertHistoryToNeuralTrainingData(History history) {
        System.out.println("-------convertHistoryToTrainingData-----");
        try {
            // Create decision object
            String decisionJsonStr = history.getDecision();
            History.PlayerDecisionCell decisionObj = objectMapper.readValue(decisionJsonStr, History.PlayerDecisionCell.class);


            int[] cellOwners = new int[decisionObj.getFields().size()];
            int[] cellDefendersSize = new int[decisionObj.getFields().size()];
            for (Map.Entry<Integer, History.PlayerDecisionCell.Field> fieldEntry : decisionObj.getFields().entrySet()) {
                int fieldIndex = fieldEntry.getKey();
                int playerKey = -1;
                if (fieldEntry.getValue().rankIndex != null) {
                    playerKey = fieldEntry.getValue().rankIndex;
                }

                cellOwners[fieldIndex] = playerKey;
                cellDefendersSize[fieldIndex] = fieldEntry.getValue().getDefenders();
            }

            //Output
            HelgaInputData trainingData = new HelgaInputData();
            trainingData.setAttackPower(decisionObj.getReserveSize());
            trainingData.setEnemiesCount(decisionObj.getEnemiesCount());

            trainingData.setCellOwners(cellOwners);
            trainingData.setCellDefendersSize(cellDefendersSize);

            trainingData.setChosenTarget(decisionObj.getDecision().getTarget());
            trainingData.setChosenTroopSize(decisionObj.getDecision().getTroops());


            return trainingData;
        } catch (Exception e) {
            System.out.println(">>>>>>>>CONVERSION ERROR");

            //e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /// ///////////////////////////////////////////////////////////////////////////////////////////////////////
    ///  QUEUE METHODS  //////////////////////////////////////////////////////////////////////////////////////
    /// ///////////////////////////////////////////////////////////////////////////////////////////////////////
    public void queueHistoryRead(AiHistoryQueueItem item) {
        log.error("HELGA-QUEUE[-History-].Reader: {}\n", item);
//        PlayState playState = item.getPlayState();
//        GameState gameState = playState.getGameState();

    }

}
