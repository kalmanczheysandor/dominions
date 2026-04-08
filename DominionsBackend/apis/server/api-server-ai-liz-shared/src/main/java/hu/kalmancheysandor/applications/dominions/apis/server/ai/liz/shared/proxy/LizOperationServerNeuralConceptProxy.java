package hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.proxy;


import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.dto.concept.*;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.dto.concept.execution.LizNeuralConceptExecutionStatusResponse;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.dto.concept.result.LizNeuralConceptExecutionChartDataItemResponse;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.dto.concept.result.LizNeuralConceptSnapshotChartDataItemResponse;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.dto.concept.result.option.LizNeuralConceptResultHistoryPlayerOptionResponse;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.dto.concept.result.option.LizNeuralConceptResultHistoryScenarioOptionResponse;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(
        name = "server-ai-liz-operation",
        contextId = "LizOperationServerNeuralConceptProxy",
        path = "/concept"
)
public interface LizOperationServerNeuralConceptProxy {

    @GetMapping("/{uuid}")
    public LizNeuralConceptAccessResponse access(@PathVariable("uuid") String conceptUuid);

    @GetMapping("/list")
    public List<LizNeuralConceptItemResponse> listAll();
    @PostMapping("/add")
    public LizNeuralConceptCreateResponse add(@Valid @RequestBody LizNeuralConceptCreateRequest request);

    @PostMapping("/{uuid}/edit")
    public LizNeuralConceptUpdateResponse edit(@PathVariable("uuid") String conceptUuid, @Valid @RequestBody LizNeuralConceptUpdateRequest request);

    @DeleteMapping("/{uuid}/delete")
    public void delete(@PathVariable("uuid") String conceptUuid);

    @DeleteMapping("/delete")
    public void delete(@Valid @RequestBody LizNeuralConceptDeleteRequest request);

    /// //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /// RESULT METHODS ///////////////////////////////////////////////////////////////////////////////////////////////
    /// //////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @GetMapping("/{uuid}/result/chart/snapshot/{scenarioUuid}/{playerUuid}")
    public LizNeuralConceptSnapshotChartDataItemResponse snapshotChartData(@PathVariable("uuid") String conceptUuid, @PathVariable("scenarioUuid") String scenarioUuid, @PathVariable("playerUuid") String playerUuid);

    @GetMapping("/{uuid}/result/chart/execution/{scenarioUuid}/{playerUuid}")
    public LizNeuralConceptExecutionChartDataItemResponse executionChartData(@PathVariable("uuid") String conceptUuid, @PathVariable("scenarioUuid") String scenarioUuid, @PathVariable("playerUuid") String playerUuid);

    @GetMapping("/{uuid}/result/options/scenario/{scenarioUuid}/player")
    public List<LizNeuralConceptResultHistoryPlayerOptionResponse> atResultListHistoryPlayer(@PathVariable("uuid") String conceptUuid,@PathVariable("scenarioUuid") String scenarioUuid);

    @GetMapping("/{uuid}/result/options/scenario")
    public List<LizNeuralConceptResultHistoryScenarioOptionResponse> atResultListHistoryScenario(@PathVariable("uuid") String conceptUuid);

    /// //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /// EXECUTION METHODS ////////////////////////////////////////////////////////////////////////////////////////////
    /// //////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @GetMapping("/{uuid}/execution/status")
    public LizNeuralConceptExecutionStatusResponse executionStatus(@PathVariable("uuid") String conceptUuid);

    @GetMapping("/{uuid}/execution/action/start")
    public void executionStartAction(@PathVariable("uuid") String conceptUuid);

    @GetMapping("/{uuid}/execution/action/pause")
    public void executionPauseAction(@PathVariable("uuid") String conceptUuid);

    @GetMapping("/{uuid}/execution/action/continue")
    public void executionContinueAction(@PathVariable("uuid") String conceptUuid);

    @GetMapping("/{uuid}/execution/action/cancel")
    public void executionCancelAction(@PathVariable("uuid") String conceptUuid);

}
