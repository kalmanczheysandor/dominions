package hu.kalmancheysandor.applications.dominions.servers.ai.liz.controller;


import hu.kalmancheysandor.applications.dominions.apis.server.ai.common.dto.AiDecisionRequest;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.common.dto.AiDecisionResponse;
import hu.kalmancheysandor.applications.dominions.servers.ai.liz.service.LizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class LizController {
    @Autowired
    private LizService lizService;

    @PostMapping("/call")
    @ResponseStatus(HttpStatus.OK)
    public AiDecisionResponse generateResponse(@RequestBody AiDecisionRequest request) {
        return lizService.generateResponse(request);
    }

//    @GetMapping("/train")
//    @ResponseStatus(HttpStatus.OK)
//    public void train() {
//        lizService.train();
//    }
//


    @GetMapping("/training/start")
    @ResponseStatus(HttpStatus.OK)
    public void trainingStart() {
        lizService.train();
    }


    @GetMapping("/check")
    @ResponseStatus(HttpStatus.OK)
    public void learn() {
        lizService.check();
    }


    @DeleteMapping("/scenario/delete/{scenarioId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteScenario(@PathVariable("scenarioId") int scenarioId) {
        lizService.deleteScenario(scenarioId);
    }

}
