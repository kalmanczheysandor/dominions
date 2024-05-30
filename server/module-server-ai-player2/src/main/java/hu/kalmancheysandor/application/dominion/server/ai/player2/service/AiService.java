package hu.kalmancheysandor.application.dominion.server.ai.player2.service;

import hu.kalmancheysandor.application.dominion.api.ai.common.IAiEngine;
import hu.kalmancheysandor.application.dominion.api.ai.common.AiRequest;
import hu.kalmancheysandor.application.dominion.api.ai.common.AiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AiService {
    @Autowired
    private IAiEngine IAiEngine;

    public AiResponse generateResponse(AiRequest request) {
        return IAiEngine.generateResponse(request);
    }
}
