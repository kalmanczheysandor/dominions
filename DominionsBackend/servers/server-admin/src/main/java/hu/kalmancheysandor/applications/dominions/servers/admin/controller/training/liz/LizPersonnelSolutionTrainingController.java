package hu.kalmancheysandor.applications.dominions.servers.admin.controller.training.liz;


import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.dto.xxx.*;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.proxy.LizTrainingServerPersonnelSolutionTrainingProxy;
import hu.kalmancheysandor.applications.dominions.apis.server.user.admin.AdminPermission;
import hu.kalmancheysandor.applications.dominions.apis.server.user.admin.service.authentication.AdminAuthenticationService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ai/liz/personnel/0/solution/{solutionUuid}/training")
@Slf4j
public class LizPersonnelSolutionTrainingController {
    @Autowired
    private LizTrainingServerPersonnelSolutionTrainingProxy lizTrainingServerPersonnelSolutionTrainingProxy;

    @Autowired
    private AdminAuthenticationService authenticationService;

    /// ///////////////////////////////////// ACCESS ////////////////////////////////////////////////////////////

    @GetMapping("/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    public LizPersonnelSolutionTrainingAccessResponse access(@PathVariable("solutionUuid") String solutionUuid, @PathVariable("uuid") String uuid) {
        // Check permission
        authenticationService.assertHasAccessPermission(AdminPermission.generator().ai().liz().character().variant().training());

        return lizTrainingServerPersonnelSolutionTrainingProxy.access(solutionUuid, uuid);
    }

    /// ///////////////////////////////////// LIST //////////////////////////////////////////////////////////////

    @GetMapping("/list")
    public List<LizPersonnelSolutionTrainingItemResponse> listAll(@PathVariable("solutionUuid") String solutionUuid) {
        // Check permission
        authenticationService.assertHasAccessPermission(AdminPermission.generator().ai().liz().character().variant().training());

        return lizTrainingServerPersonnelSolutionTrainingProxy.listAll(solutionUuid);
    }

    /// ///////////////////////////////////// ADD ///////////////////////////////////////////////////////////////
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public LizPersonnelSolutionTrainingCreateResponse add(@PathVariable("solutionUuid") String solutionUuid, @Valid @RequestBody LizPersonnelSolutionTrainingCreateRequest request) {
        // Check permission
        authenticationService.assertHasAddPermission(AdminPermission.generator().ai().liz().character().variant().training());

        return lizTrainingServerPersonnelSolutionTrainingProxy.add(solutionUuid, request);
    }


    /// ///////////////////////////////////// EDIT ///////////////////////////////////////////////////////////////

    @PostMapping("/{uuid}/edit")
    @ResponseStatus(HttpStatus.OK)
    public LizPersonnelSolutionTrainingUpdateResponse edit(@PathVariable("solutionUuid") String solutionUuid, @PathVariable("uuid") String uuid, @Valid @RequestBody LizPersonnelSolutionTrainingUpdateRequest request) {
        // Check permission
        authenticationService.assertHasEditPermission(AdminPermission.generator().ai().liz().character().variant().training());

        return lizTrainingServerPersonnelSolutionTrainingProxy.edit(solutionUuid, uuid, request);
    }


    /// ///////////////////////////////////// DELETE /////////////////////////////////////////////////////////////

    @DeleteMapping("/{uuid}/delete")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("solutionUuid") String solutionUuid, @PathVariable("uuid") String uuid) {
        // Check permission
        authenticationService.assertHasDeletePermission(AdminPermission.generator().ai().liz().character().variant().training());

        lizTrainingServerPersonnelSolutionTrainingProxy.delete(solutionUuid, uuid);
    }

    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("solutionUuid") String solutionUuid, @Valid @RequestBody LizPersonnelSolutionTrainingDeleteRequest request) {
        // Check permission
        authenticationService.assertHasDeletePermission(AdminPermission.generator().ai().liz().character().variant().training());

        lizTrainingServerPersonnelSolutionTrainingProxy.delete(solutionUuid, request);
    }

}
