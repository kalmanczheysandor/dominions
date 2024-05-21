package hu.kalmancheysandor.application.dominion.server.game.controller.game;

import hu.kalmancheysandor.application.dominion.api.ai.common.AiRequest;
import hu.kalmancheysandor.application.dominion.api.ai.common.AiResponse;
import hu.kalmancheysandor.application.dominion.server.game.service.game.dto.GameJoinResponse;
import hu.kalmancheysandor.application.dominion.server.game.service.game.GameService;
import hu.kalmancheysandor.application.dominion.server.game.service.game.dto.GameStepRequest;
import hu.kalmancheysandor.application.dominion.server.game.service.game.dto.GameStateResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@RestController
@Slf4j
public class GameController {

    @Autowired
    private GameService gameService;

    @PostMapping("/play/{sessionKey}/step")
    public GameStateResponse step(@PathVariable String sessionKey, @RequestBody GameStepRequest request ) {
        return gameService.doStep(sessionKey,request);
    }

    @GetMapping("/play/{sessionKey}/state")
    public GameStateResponse currentState(@PathVariable String sessionKey) {
        return gameService.currentState(sessionKey);
    }

}
