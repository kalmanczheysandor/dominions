package hu.kalmancheysandor.application.dominion.server.game.controller.game;

import hu.kalmancheysandor.application.dominion.api.ai.common.AiRequest;
import hu.kalmancheysandor.application.dominion.api.ai.common.AiResponse;
import hu.kalmancheysandor.application.dominion.server.game.service.game.dto.*;
import hu.kalmancheysandor.application.dominion.server.game.service.game.GameService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;



@RestController
@Slf4j
public class GameController {

    @Autowired
    private GameService gameService;


    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public GameCreateResponse create(@RequestBody GameCreateRequest request ) {
        return gameService.create(request);
    }

    @PostMapping("/{sessionKey}/join")
    @ResponseStatus(HttpStatus.OK)
    public GameJoinResponse join(@PathVariable String sessionKey,@RequestBody GameJoinRequest request ) {
        return gameService.join(sessionKey,request);
    }

    @PostMapping("/{sessionKey}/play/step")
    @ResponseStatus(HttpStatus.OK)
    public GameStateResponse step(@PathVariable String sessionKey, @RequestBody GameStepRequest request ) {
        return gameService.doStep(sessionKey,request);
    }

    @GetMapping("/{sessionKey}/play/current")
    @ResponseStatus(HttpStatus.OK)
    public GameStateResponse currentState(@PathVariable String sessionKey) {
        return gameService.currentState(sessionKey);
    }

}
