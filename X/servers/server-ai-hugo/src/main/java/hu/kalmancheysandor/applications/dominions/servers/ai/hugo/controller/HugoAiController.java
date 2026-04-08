package hu.kalmancheysandor.applications.dominions.servers.ai.hugo.controller;


import hu.kalmancheysandor.applications.dominions.apis.server.ai.common.dto.AiDecisionRequest;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.common.dto.AiDecisionResponse;
import hu.kalmancheysandor.applications.dominions.servers.ai.hugo.service.HugoAiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HugoAiController {
    @Autowired
    private HugoAiService hugoAiService;

    @PostMapping("/call")
    public AiDecisionResponse generateResponse(@RequestBody AiDecisionRequest request) {
        return hugoAiService.generateResponse(request);
    }
}
