package hu.kalmancheysandor.application.dominion.server.ai.liz.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.kalmancheysandor.application.dominion.api.ai.common.AiRequest;
import hu.kalmancheysandor.application.dominion.api.ai.common.AiResponse;
import hu.kalmancheysandor.application.dominion.api.ai.common.neural.INeuralAiEngine;
import hu.kalmancheysandor.application.dominion.api.ai.common.neural.NeuralTrainingData;
import hu.kalmancheysandor.application.dominion.server.ai.liz.repository.HistoryRepository;
import hu.kalmancheysandor.application.dominion.server.ai.liz.repository.domain.HistoryRow;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class LizService {
    @Autowired
    private INeuralAiEngine engine;

    @Autowired
    private HistoryRepository historyRepository;

    @Autowired
    private ObjectMapper objectMapper;

    public AiResponse generateResponse(AiRequest request) {
        return engine.generateResponse(request);
    }

    public void train() {
        Map<String, List<HistoryRow>> groups = splitHistoryIntoGroups(historyRepository.findAll());
        for (Map.Entry<String, List<HistoryRow>> groupEntry : groups.entrySet()) {
            engine.train(groupEntry.getKey(), convertHistoryListToTrainingDataList(groupEntry.getValue()));
        }
    }

    public void check() {
        Map<String, List<HistoryRow>> groups = splitHistoryIntoGroups(historyRepository.findAll());
        for (Map.Entry<String, List<HistoryRow>> groupEntry : groups.entrySet()) {
            engine.check(groupEntry.getKey(), convertHistoryListToTrainingDataList(groupEntry.getValue()));
        }
    }

    private Map<String, List<HistoryRow>> splitHistoryIntoGroups(List<HistoryRow> historyList) {
        Map<String, List<HistoryRow>> groups = new HashMap<>();
        for (HistoryRow history : historyList) {
            String playerCode = history.getPlayer();
            if (groups.containsKey(playerCode)) {
                groups.get(playerCode).add(history);
            } else {
                groups.put(playerCode, new ArrayList<>());
            }
        }
        return groups;
    }

    private List<NeuralTrainingData> convertHistoryListToTrainingDataList(List<HistoryRow> historyList) {
        List<NeuralTrainingData> trainingDataList = new ArrayList<>();
        for (HistoryRow history : historyList) {
            trainingDataList.add(convertHistoryToTrainingData(history));
        }
        return trainingDataList;
    }

    private NeuralTrainingData convertHistoryToTrainingData(HistoryRow historyRow) {

        try {

            NeuralTrainingData trainingData = new NeuralTrainingData();

            String decisionJsonStr = historyRow.getDecision();
            HistoryRow.PlayerDecisionCell decisionObj = objectMapper.readValue(decisionJsonStr, HistoryRow.PlayerDecisionCell.class);

            int[] cellOwners = new int[decisionObj.getFields().size()];
            int[] cellDefendersSize = new int[decisionObj.getFields().size()];
            for (Map.Entry<Integer, HistoryRow.PlayerDecisionCell.Field> fieldEntry : decisionObj.getFields().entrySet()) {
                int fieldIndex = fieldEntry.getKey();
                int playerKey = -1;
                if (fieldEntry.getValue().playerKey != null) {
                    playerKey = fieldEntry.getValue().playerKey;
                }

                cellOwners[fieldIndex] = playerKey;
                cellDefendersSize[fieldIndex] = fieldEntry.getValue().getDefenders();
            }
            trainingData.setCellOwners(cellOwners);
            trainingData.setCellDefendersSize(cellDefendersSize);
            trainingData.setChosenTarget(decisionObj.getDecision().getTarget());
            trainingData.setChosenTroopSize(decisionObj.getDecision().getTroops());
            trainingData.setReserveSize(decisionObj.getReserveSize());
            trainingData.setEnemiesCount(decisionObj.getEnemiesCount());

            return trainingData;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
