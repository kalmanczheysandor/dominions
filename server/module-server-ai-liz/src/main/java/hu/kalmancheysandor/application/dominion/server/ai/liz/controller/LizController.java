package hu.kalmancheysandor.application.dominion.server.ai.liz.controller;


import hu.kalmancheysandor.application.dominion.api.ai.common.AiRequest;
import hu.kalmancheysandor.application.dominion.api.ai.common.AiResponse;
import hu.kalmancheysandor.application.dominion.server.ai.liz.service.LizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LizController {
    @Autowired
    private LizService lizService;

    @PostMapping("/call")
    public AiResponse generateResponse(@RequestBody AiRequest request) {
        return lizService.generateResponse(request);
    }

    @GetMapping("/learn")
    public void learn() {
        lizService.learn();
    }



}
