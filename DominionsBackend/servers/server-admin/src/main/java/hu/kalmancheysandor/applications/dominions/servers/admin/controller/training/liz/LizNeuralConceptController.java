package hu.kalmancheysandor.applications.dominions.servers.admin.controller.training.liz;


import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.dto.concept.*;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.dto.concept.execution.LizNeuralConceptExecutionStatusResponse;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.dto.concept.result.LizNeuralConceptExecutionChartDataItemResponse;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.dto.concept.result.LizNeuralConceptSnapshotChartDataItemResponse;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.dto.concept.result.option.LizNeuralConceptResultHistoryPlayerOptionResponse;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.dto.concept.result.option.LizNeuralConceptResultHistoryScenarioOptionResponse;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.proxy.LizOperationServerNeuralConceptProxy;
import hu.kalmancheysandor.applications.dominions.apis.server.user.admin.AdminPermission;
import hu.kalmancheysandor.applications.dominions.apis.server.user.admin.service.authentication.AdminAuthenticationService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ai/liz/concept")
@Slf4j
public class LizNeuralConceptController {
    @Autowired
    private LizOperationServerNeuralConceptProxy lizOperationServerNeuralConceptProxy;

    @Autowired
    private AdminAuthenticationService authenticationService;

    /// ///////////////////////////////////// ACCESS ////////////////////////////////////////////////////////////

    @GetMapping("/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    public LizNeuralConceptAccessResponse access(@PathVariable("uuid") String uuid) {
        // Check permission
        authenticationService.assertHasAccessPermission(AdminPermission.generator().ai().liz().concept());

        return lizOperationServerNeuralConceptProxy.access(uuid);
    }

    /// ///////////////////////////////////// LIST //////////////////////////////////////////////////////////////

    @GetMapping("/list")
    public List<LizNeuralConceptItemResponse> listAll() {
        // Check permission
        authenticationService.assertHasAccessPermission(AdminPermission.generator().ai().liz().concept());

        return lizOperationServerNeuralConceptProxy.listAll();
    }

    /// ///////////////////////////////////// ADD ///////////////////////////////////////////////////////////////
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public LizNeuralConceptCreateResponse add(@Valid @RequestBody LizNeuralConceptCreateRequest request) {
        // Check permission
        authenticationService.assertHasAddPermission(AdminPermission.generator().ai().liz().concept());

        return lizOperationServerNeuralConceptProxy.add(request);
    }


    /// ///////////////////////////////////// EDIT ///////////////////////////////////////////////////////////////

    @PostMapping("/{uuid}/edit")
    @ResponseStatus(HttpStatus.OK)
    public LizNeuralConceptUpdateResponse edit(@PathVariable("uuid") String uuid, @Valid @RequestBody LizNeuralConceptUpdateRequest request) {
        // Check permission
        authenticationService.assertHasEditPermission(AdminPermission.generator().ai().liz().concept());

        return lizOperationServerNeuralConceptProxy.edit(uuid, request);
    }


    /// ///////////////////////////////////// DELETE /////////////////////////////////////////////////////////////

    @DeleteMapping("/{uuid}/delete")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("uuid") String uuid) {
        // Check permission
        authenticationService.assertHasDeletePermission(AdminPermission.generator().ai().liz().concept());

        lizOperationServerNeuralConceptProxy.delete(uuid);
    }

    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@Valid @RequestBody LizNeuralConceptDeleteRequest request) {
        // Check permission
        authenticationService.assertHasDeletePermission(AdminPermission.generator().ai().liz().concept());

        lizOperationServerNeuralConceptProxy.delete(request);
    }


    /// //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /// RESULT METHODS ///////////////////////////////////////////////////////////////////////////////////////////////
    /// //////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @GetMapping("/{uuid}/result/chart/snapshot/{scenarioUuid}/{playerUuid}")
    @ResponseStatus(HttpStatus.OK)
    public LizNeuralConceptSnapshotChartDataItemResponse snapshotChartData(@PathVariable("uuid") String uuid, @PathVariable("scenarioUuid") String scenarioUuid, @PathVariable("playerUuid") String playerUuid) {
        // Check permission
        authenticationService.assertHasAccessPermission(AdminPermission.generator().ai().liz().concept());
        System.out.println("RESULT-chart-snapshot > scenarioUuid:" + scenarioUuid + " playerUuid:" + playerUuid);
        return lizOperationServerNeuralConceptProxy.snapshotChartData(uuid, scenarioUuid, playerUuid);
    }

    @GetMapping("/{uuid}/result/chart/execution/{scenarioUuid}/{playerUuid}")
    @ResponseStatus(HttpStatus.OK)
    public LizNeuralConceptExecutionChartDataItemResponse executionChartData(@PathVariable("uuid") String uuid, @PathVariable("scenarioUuid") String scenarioUuid, @PathVariable("playerUuid") String playerUuid) {
        // Check permission
        authenticationService.assertHasAccessPermission(AdminPermission.generator().ai().liz().concept());
        System.out.println("RESULT-chart-execution > scenarioUuid:" + scenarioUuid + " playerUuid:" + playerUuid);
        return lizOperationServerNeuralConceptProxy.executionChartData(uuid, scenarioUuid, playerUuid);
    }

    @GetMapping("/{uuid}/result/options/scenario")
    public List<LizNeuralConceptResultHistoryScenarioOptionResponse> atResultListHistoryScenario(@PathVariable("uuid") String uuid) {
        return lizOperationServerNeuralConceptProxy.atResultListHistoryScenario(uuid);
    }

    @GetMapping("/{uuid}/result/options/scenario/{scenarioUuid}/player")
    public List<LizNeuralConceptResultHistoryPlayerOptionResponse> atResultListHistoryPlayer(@PathVariable("uuid") String uuid, @PathVariable("scenarioUuid") String scenarioUuid) {
        return lizOperationServerNeuralConceptProxy.atResultListHistoryPlayer(uuid, scenarioUuid);
    }


    /// //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /// EXECUTION METHODS ////////////////////////////////////////////////////////////////////////////////////////////
    /// //////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @GetMapping("/{uuid}/execution/status")
    @ResponseStatus(HttpStatus.OK)
    public LizNeuralConceptExecutionStatusResponse executionStatus(@PathVariable("uuid") String uuid) {
        System.out.println("EXECUTION-STATUS");
        return lizOperationServerNeuralConceptProxy.executionStatus(uuid);
    }

    @GetMapping("/{uuid}/execution/action/start")
    @ResponseStatus(HttpStatus.OK)
    public void executionStart(@PathVariable("uuid") String uuid) {
        System.out.println("EXECUTION:START");
        lizOperationServerNeuralConceptProxy.executionStartAction(uuid);
    }

    @GetMapping("/{uuid}/execution/action/cancel")
    @ResponseStatus(HttpStatus.OK)
    public void executionCancel(@PathVariable("uuid") String uuid) {
        System.out.println("EXECUTION:CANCEL");
        lizOperationServerNeuralConceptProxy.executionCancelAction(uuid);
    }

    @GetMapping("/{uuid}/execution/action/pause")
    @ResponseStatus(HttpStatus.OK)
    public void executionPause(@PathVariable("uuid") String uuid) {
        System.out.println("EXECUTION:Pause");
        lizOperationServerNeuralConceptProxy.executionPauseAction(uuid);
    }


    @GetMapping("/{uuid}/execution/action/continue")
    @ResponseStatus(HttpStatus.OK)
    public void executionContinue(@PathVariable("uuid") String uuid) {
        System.out.println("EXECUTION:Continue");
        lizOperationServerNeuralConceptProxy.executionContinueAction(uuid);
    }
}
