package hu.kalmancheysandor.applications.dominions.servers.game.controller;


import hu.kalmancheysandor.applications.dominions.apis.server.game.common.dto.game.*;
import hu.kalmancheysandor.applications.dominions.apis.server.game.common.dto.game.action.GamePlayAttackActionRequest;
import hu.kalmancheysandor.applications.dominions.apis.server.game.common.dto.game.action.GamePlayAttackActionResponse;
import hu.kalmancheysandor.applications.dominions.apis.server.game.common.dto.game.action.GamePlayReserveActionRequest;
import hu.kalmancheysandor.applications.dominions.apis.server.game.common.dto.game.action.GamePlayReserveActionResponse;
import hu.kalmancheysandor.applications.dominions.apis.server.game.common.dto.game.scenario.GameScenarioItemResponse;
import hu.kalmancheysandor.applications.dominions.servers.game.service.game.GameService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class GameController {

    @Autowired
    private GameService gameService;

    @Autowired
    private StreamBridge streamBridge;


    @GetMapping("/list/recruiting")
    public List<GamePlayItemResponse> listAllRecruitingGamePlay() {
        return gameService.listAllRecruitingGamePlay();
    }

    @GetMapping("/list/scenario")
    public List<GameScenarioItemResponse> listAllPublishedGameScenario() {
        return gameService.listAllPublishedGameScenario();
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public GamePlayCreateResponse createGamePlay(@Valid @RequestBody GamePlayCreateRequest request) {
        return gameService.createGamePlay(request);
    }


    @PostMapping("/join")
    @ResponseStatus(HttpStatus.OK)
    public GamePlayJoinResponse joinToGamePlay(@NotNull @RequestBody GamePlayJoinRequest request) {

        return gameService.joinToGamePlay(request);
    }


    @PostMapping("/state")
    @ResponseStatus(HttpStatus.OK)
    public GamePlayStateResponse stateOfGamePlay(@NotNull @RequestBody GamePlayStateRequest request) {
        return gameService.stateOfGamePlay(request);
    }


    @PostMapping("/action/attack")
    @ResponseStatus(HttpStatus.OK)
    public GamePlayAttackActionResponse sendAttackActionToGamePlay(@NotNull @RequestBody GamePlayAttackActionRequest request) {

        return gameService.sendAttackActionToGamePlay(request);
    }


    @PostMapping("/action/reserve")
    @ResponseStatus(HttpStatus.OK)
    public GamePlayReserveActionResponse sendReserveActionToGamePlay(@NotNull @RequestBody GamePlayReserveActionRequest request) {
        return gameService.sendReserveActionToGamePlay(request);
    }


}
