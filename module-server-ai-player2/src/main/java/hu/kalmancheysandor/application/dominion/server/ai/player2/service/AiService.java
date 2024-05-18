package hu.kalmancheysandor.application.dominion.server.ai.player2.service;

import hu.kalmancheysandor.application.dominion.api.ai.common.AiEngine;
import hu.kalmancheysandor.application.dominion.api.ai.common.AiRequest;
import hu.kalmancheysandor.application.dominion.api.ai.common.AiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AiService {
    @Autowired
    private AiEngine aiEngine;

    public AiResponse generateResponse(AiRequest request) {
        return aiEngine.generateResponse(request);
    }
}
