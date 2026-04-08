package hu.kalmancheysandor.applications.dominions.apis.server.ai.common.service;

import hu.kalmancheysandor.applications.dominions.apis.ai.common.AiDecisionContext;
import hu.kalmancheysandor.applications.dominions.apis.ai.common.AiDecisionResult;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.common.dto.AiDecisionRequest;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.common.dto.AiDecisionResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class TAiService {
    @Autowired
    private ModelMapper modelMapper;

    protected AiDecisionContext convertRequestToDecisionContext(AiDecisionRequest request) {
        return modelMapper.map(request, AiDecisionContext.class);
    }

    protected AiDecisionResponse convertDecisionResultToResponse(AiDecisionResult decisionResult) {
        return modelMapper.map(decisionResult, AiDecisionResponse.class);
    }

}
