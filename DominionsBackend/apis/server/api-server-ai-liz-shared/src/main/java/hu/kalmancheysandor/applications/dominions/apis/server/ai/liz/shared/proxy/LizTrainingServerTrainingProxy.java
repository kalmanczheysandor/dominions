package hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.proxy;


import hu.kalmancheysandor.applications.dominions.apis.server.common.configuration.feign.PrimaryFeignProxyConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@FeignClient(
        name = "server-ai-liz-training",
        contextId = "LizTrainingServerTrainingProxy",
        configuration = PrimaryFeignProxyConfig.class,
        path = "/training"
)
public interface LizTrainingServerTrainingProxy {

    @DeleteMapping("/scenario/delete/{scenarioId}")
    void deleteScenario(@PathVariable("scenarioId") int scenarioId);

    @GetMapping("/start")
    public void trainingStart();






//
//
//
//
//
//
//
//    @PostMapping("/create")
//    public GamePlayCreateResponse createGamePlay(@Valid @RequestBody GamePlayCreateRequest request);
//
//    @PostMapping("/join")
//    public GamePlayJoinResponse joinToGamePlay(@Valid @RequestBody GamePlayJoinRequest request);
//
//    @PostMapping("/state")
//    public GamePlayStateResponse stateOfGamePlay(@NotNull @RequestBody GamePlayStateRequest request);
//
//    @PostMapping("/action/attack")
//    public GamePlayAttackActionResponse sendAttackActionToGamePlay(@Valid @RequestBody GamePlayAttackActionRequest request);
//
//
//    @PostMapping("/action/reserve")
//    public GamePlayReserveActionResponse sendReserveActionToGamePlay(@Valid @RequestBody GamePlayReserveActionRequest request);
//
//
//    @GetMapping("/list")
//    public List<GamePlayItemResponse> listAllGamePlay();
//
//    @GetMapping("/list/recruiting")
//    public List<GamePlayItemResponse> listAllRecruitingGamePlay();
//
//    @GetMapping("/list/scenario")
//    public List<GameScenarioItemResponse> listAllPublishedGameScenario();
}
