package hu.kalmancheysandor.applications.dominions.servers.ai.helga.controller;


import hu.kalmancheysandor.applications.dominions.apis.server.ai.common.dto.AiDecisionRequest;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.common.dto.AiDecisionResponse;
import hu.kalmancheysandor.applications.dominions.servers.ai.helga.service.HelgaAiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class HelgaAiController {
    @Autowired
    private HelgaAiService helgaAiService;

    @PostMapping("/call")
    public AiDecisionResponse generateResponse(@RequestBody AiDecisionRequest request) {
        return helgaAiService.generateResponse(request);
    }



    @GetMapping("/train")
    @ResponseStatus(HttpStatus.OK)
    public void train() {
        helgaAiService.train();
    }

    @GetMapping("/check")
    @ResponseStatus(HttpStatus.OK)
    public void learn() {
        helgaAiService.check();
    }
}
