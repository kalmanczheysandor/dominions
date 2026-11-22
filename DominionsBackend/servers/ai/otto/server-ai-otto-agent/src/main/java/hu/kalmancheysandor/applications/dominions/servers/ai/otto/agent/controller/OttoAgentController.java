package hu.kalmancheysandor.applications.dominions.servers.ai.otto.agent.controller;


import hu.kalmancheysandor.applications.dominions.apis.server.ai.common.dto.AiDecisionRequest;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.common.dto.AiDecisionResponse;
import hu.kalmancheysandor.applications.dominions.servers.ai.otto.agent.service.OttoAgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OttoAgentController {
    @Autowired
    private OttoAgentService ottoAgentService;

    @PostMapping("/call")
    public AiDecisionResponse generateResponse(@RequestBody AiDecisionRequest request) {
        return ottoAgentService.generateResponse(request);
    }
}
