package hu.kalmancheysandor.application.dominion.server.ai.liz.controller;


import hu.kalmancheysandor.application.dominion.api.ai.common.AiRequest;
import hu.kalmancheysandor.application.dominion.api.ai.common.AiResponse;
import hu.kalmancheysandor.application.dominion.server.ai.liz.service.AiTrainingRequest;
import hu.kalmancheysandor.application.dominion.server.ai.liz.service.LizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class LizController {
    @Autowired
    private LizService lizService;

    @PostMapping("/call")
    @ResponseStatus(HttpStatus.OK)
    public AiResponse generateResponse(@RequestBody AiRequest request) {
        return lizService.generateResponse(request);
    }

    @GetMapping("/train")
    @ResponseStatus(HttpStatus.OK)
    public void learn() {
        lizService.train();
    }
}
