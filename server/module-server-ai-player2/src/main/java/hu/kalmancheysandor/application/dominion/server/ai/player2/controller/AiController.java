package hu.kalmancheysandor.application.dominion.server.ai.player2.controller;


import hu.kalmancheysandor.application.dominion.api.ai.common.AiRequest;
import hu.kalmancheysandor.application.dominion.api.ai.common.AiResponse;
import hu.kalmancheysandor.application.dominion.server.ai.player2.service.AiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AiController {
    @Autowired
    private AiService aiService;

    @PostMapping("/call")
    public AiResponse generateResponse(@RequestBody AiRequest request) {
        return aiService.generateResponse(request);
    }
}
