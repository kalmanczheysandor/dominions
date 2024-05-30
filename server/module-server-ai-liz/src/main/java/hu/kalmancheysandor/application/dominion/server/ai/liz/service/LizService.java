package hu.kalmancheysandor.application.dominion.server.ai.liz.service;

import hu.kalmancheysandor.application.dominion.api.ai.common.AiRequest;
import hu.kalmancheysandor.application.dominion.api.ai.common.AiResponse;
import hu.kalmancheysandor.application.dominion.api.ai.common.neural.INeuralAiEngine;
import hu.kalmancheysandor.application.dominion.api.ai.common.neural.NeuralTrainingData;
import hu.kalmancheysandor.application.dominion.server.ai.liz.repository.HistoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class LizService {
    @Autowired
    private INeuralAiEngine engine;

    @Autowired
    private HistoryRepository historyRepository;

    public AiResponse generateResponse(AiRequest request) {
        return engine.generateResponse(request);
    }

    public void train(){
        List<NeuralTrainingData> trainingDataList   = new ArrayList<>();
        historyRepository.findAll();


        engine.train(trainingDataList);
    }
}
