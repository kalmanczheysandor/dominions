package hu.kalmancheysandor.applications.dominions.servers.ai.otto.agent.service;


import hu.kalmancheysandor.applications.dominions.apis.ai.common.AiDecisionContext;
import hu.kalmancheysandor.applications.dominions.apis.ai.common.AiDecisionResult;
import hu.kalmancheysandor.applications.dominions.apis.ai.common.IAiEngine;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.common.dto.AiDecisionRequest;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.common.dto.AiDecisionResponse;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.common.service.TAiService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OttoAgentService extends TAiService {
    @Autowired
    private IAiEngine engine;

    @Autowired
    private ModelMapper modelMapper;

    public AiDecisionResponse generateResponse(AiDecisionRequest request) {
        AiDecisionContext context = convertRequestToDecisionContext(request);
        AiDecisionResult result = engine.makeDecision(context);

        // Generate response
        return convertDecisionResultToResponse(result);
    }


}
