package hu.kalmancheysandor.applications.dominions.servers.ai.liz.agent.controller;


import hu.kalmancheysandor.applications.dominions.apis.server.ai.common.dto.AiDecisionRequest;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.common.dto.AiDecisionResponse;
import hu.kalmancheysandor.applications.dominions.servers.ai.liz.agent.service.LizAgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class LizAgentController {
    @Autowired
    private LizAgentService lizAgentService;

    @PostMapping("/call")
    @ResponseStatus(HttpStatus.OK)
    public AiDecisionResponse generateResponse(@RequestBody AiDecisionRequest request) {
        return lizAgentService.generateResponse(request);
    }
}
