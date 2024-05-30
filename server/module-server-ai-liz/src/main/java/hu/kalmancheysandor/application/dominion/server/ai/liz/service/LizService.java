package hu.kalmancheysandor.application.dominion.server.ai.liz.service;

import hu.kalmancheysandor.application.dominion.api.ai.common.AiRequest;
import hu.kalmancheysandor.application.dominion.api.ai.common.AiResponse;
import hu.kalmancheysandor.application.dominion.api.ai.common.INeuralAiEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LizService {
    @Autowired
    private INeuralAiEngine engine;

    public AiResponse generateResponse(AiRequest request) {
        return engine.generateResponse(request);
    }

    public void learn() {
        engine.learn();
    }
}
