package hu.kalmancheysandor.applications.dominions.apis.server.game.common.proxy;


import hu.kalmancheysandor.applications.dominions.apis.server.common.configuration.feign.PrimaryFeignProxyConfig;
import hu.kalmancheysandor.applications.dominions.apis.server.game.common.dto.game.*;
import hu.kalmancheysandor.applications.dominions.apis.server.game.common.dto.game.action.GamePlayAttackActionRequest;
import hu.kalmancheysandor.applications.dominions.apis.server.game.common.dto.game.action.GamePlayAttackActionResponse;
import hu.kalmancheysandor.applications.dominions.apis.server.game.common.dto.game.action.GamePlayReserveActionRequest;
import hu.kalmancheysandor.applications.dominions.apis.server.game.common.dto.game.action.GamePlayReserveActionResponse;
import hu.kalmancheysandor.applications.dominions.apis.server.game.common.dto.game.scenario.GameScenarioItemResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(
        name = "server-game",
        contextId = "GameServerProxy",
        configuration = PrimaryFeignProxyConfig.class
)
public interface GameServerProxy {
    @PostMapping("/create")
    public GamePlayCreateResponse createGamePlay(@Valid @RequestBody GamePlayCreateRequest request);

    @PostMapping("/join")
    public GamePlayJoinResponse joinToGamePlay(@Valid @RequestBody GamePlayJoinRequest request);

    @PostMapping("/state")
    public GamePlayStateResponse stateOfGamePlay(@NotNull @RequestBody GamePlayStateRequest request);

    @PostMapping("/action/attack")
    public GamePlayAttackActionResponse sendAttackActionToGamePlay(@Valid @RequestBody GamePlayAttackActionRequest request);


    @PostMapping("/action/reserve")
    public GamePlayReserveActionResponse sendReserveActionToGamePlay(@Valid @RequestBody GamePlayReserveActionRequest request);


    @GetMapping("/list")
    public List<GamePlayItemResponse> listAllGamePlay();

    @GetMapping("/list/recruiting")
    public List<GamePlayItemResponse> listAllRecruitingGamePlay();

    @GetMapping("/list/scenario")
    public List<GameScenarioItemResponse> listAllPublishedGameScenario();
}
