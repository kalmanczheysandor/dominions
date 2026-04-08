package hu.kalmancheysandor.applications.dominions.servers.ai.liz.operation.controller.neurl;


import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.dto.concept.*;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.dto.concept.execution.LizNeuralConceptExecutionStatusResponse;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.dto.concept.result.LizNeuralConceptExecutionChartDataItemResponse;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.dto.concept.result.LizNeuralConceptSnapshotChartDataItemResponse;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.dto.concept.result.option.LizNeuralConceptResultHistoryPlayerOptionResponse;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.dto.concept.result.option.LizNeuralConceptResultHistoryScenarioOptionResponse;
import hu.kalmancheysandor.applications.dominions.servers.ai.liz.operation.service.neural.LizNeuralConceptService;
import hu.kalmancheysandor.applications.dominions.servers.ai.liz.operation.service.neural.LizNeuralOrchestrationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/concept")
@Slf4j
public class LizNeuralConceptController {
    @Autowired
    private LizNeuralConceptService lizNeuralConceptService;

    @Autowired
    LizNeuralOrchestrationService lizNeuralOrchestrationService;


    /// ///////////////////////////////////// ACCESS ////////////////////////////////////////////////////////////

    @GetMapping("/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    public LizNeuralConceptAccessResponse access(@PathVariable("uuid") String uuid) {
        return lizNeuralConceptService.access(uuid);
    }

    /// ///////////////////////////////////// LIST //////////////////////////////////////////////////////////////

    @GetMapping("/list")
    public List<LizNeuralConceptItemResponse> listAll() {
        return lizNeuralConceptService.listAll();
    }

    /// ///////////////////////////////////// ADD ///////////////////////////////////////////////////////////////
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public LizNeuralConceptCreateResponse add(@Valid @RequestBody LizNeuralConceptCreateRequest request) {
        return lizNeuralConceptService.save(request);
    }


    /// ///////////////////////////////////// EDIT ///////////////////////////////////////////////////////////////

    @PostMapping("/{uuid}/edit")
    @ResponseStatus(HttpStatus.OK)
    public LizNeuralConceptUpdateResponse edit(@PathVariable("uuid") String uuid, @Valid @RequestBody LizNeuralConceptUpdateRequest request) {
        return lizNeuralConceptService.update(uuid, request);
    }


    /// ///////////////////////////////////// DELETE /////////////////////////////////////////////////////////////
    @DeleteMapping("/{uuid}/delete")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("uuid") String uuid) {
        lizNeuralConceptService.deleteOneLizPersonnel(uuid);
    }

    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@Valid @RequestBody LizNeuralConceptDeleteRequest request) {
        lizNeuralConceptService.deleteMultiple(request);
    }

    /// ///////////////////////////////////// RESULT /////////////////////////////////////////////////////////////
    @GetMapping("/{uuid}/result/chart/snapshot/{scenarioUuid}/{playerUuid}")
    @ResponseStatus(HttpStatus.OK)
    public LizNeuralConceptSnapshotChartDataItemResponse snapshotChartData(@PathVariable("uuid") String uuid, @PathVariable("scenarioUuid") String scenarioUuid, @PathVariable("playerUuid") String playerUuid) {
        System.out.println("RESULT-chart-snapshot:scenarioUuid:" + scenarioUuid + " playerUuid:" + playerUuid);
        return lizNeuralConceptService.snapshotChartData(uuid, scenarioUuid, playerUuid);
    }


    @GetMapping("/{uuid}/result/chart/execution/{scenarioUuid}/{playerUuid}")
    @ResponseStatus(HttpStatus.OK)
    public LizNeuralConceptExecutionChartDataItemResponse executionChartData(@PathVariable("uuid") String uuid, @PathVariable("scenarioUuid") String scenarioUuid, @PathVariable("playerUuid") String playerUuid) {
        System.out.println("RESULT-chart-snapshot:scenarioUuid:" + scenarioUuid + " playerUuid:" + playerUuid);
        return lizNeuralConceptService.executionChartData(uuid, scenarioUuid, playerUuid);
    }


    @GetMapping("/{uuid}/result/options/scenario")
    @ResponseStatus(HttpStatus.OK)
    public List<LizNeuralConceptResultHistoryScenarioOptionResponse> atResultListHistoryScenario(@PathVariable("uuid") String uuid) {
        return lizNeuralConceptService.atResultListHistoryScenario(uuid);
    }

    @GetMapping("/{uuid}/result/options/scenario/{scenarioUuid}/player")
    @ResponseStatus(HttpStatus.OK)
    public List<LizNeuralConceptResultHistoryPlayerOptionResponse> atResultListHistoryPlayer(@PathVariable("uuid") String uuid, @PathVariable("scenarioUuid") String scenarioUuid) {
        return lizNeuralConceptService.atResultListHistoryPlayer(uuid, scenarioUuid);
    }

    /// ///////////////////////////////////// EXECUTION /////////////////////////////////////////////////////////////
    @GetMapping("/{uuid}/execution/status")
    @ResponseStatus(HttpStatus.OK)
    public LizNeuralConceptExecutionStatusResponse executionStatus(@PathVariable("uuid") String conceptUuid) {
        return lizNeuralConceptService.executionStatus(conceptUuid);
    }



    @GetMapping("/{uuid}/execution/action/start")
    @ResponseStatus(HttpStatus.OK)
    public void executionStartAction(@PathVariable("uuid") String conceptUuid) {
        lizNeuralConceptService.eventExecutionStart(conceptUuid);
    }


    @GetMapping("/{uuid}/execution/action/continue")
    @ResponseStatus(HttpStatus.OK)
    public void executionContinueAction(@PathVariable("uuid") String conceptUuid) {
        lizNeuralConceptService.eventExecutionContinue(conceptUuid);
    }

    @GetMapping("/{uuid}/execution/action/pause")
    @ResponseStatus(HttpStatus.OK)
    public void executionPauseAction(@PathVariable("uuid") String conceptUuid) {
        lizNeuralConceptService.eventExecutionPause(conceptUuid);
    }

    @GetMapping("/{uuid}/execution/action/cancel")
    @ResponseStatus(HttpStatus.OK)
    public void executionCancelAction(@PathVariable("uuid") String conceptUuid) {
        lizNeuralConceptService.eventExecutionCancel(conceptUuid);
    }
}
