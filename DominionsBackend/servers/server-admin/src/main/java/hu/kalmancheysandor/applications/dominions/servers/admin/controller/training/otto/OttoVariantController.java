package hu.kalmancheysandor.applications.dominions.servers.admin.controller.training.otto;


import hu.kalmancheysandor.applications.dominions.apis.server.ai.otto.shared.dto.variant.*;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.otto.shared.dto.variant.option.OttoVariantEvaluatorOptionResponse;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.otto.shared.proxy.OttoOperationServerVariantProxy;
import hu.kalmancheysandor.applications.dominions.apis.server.user.admin.AdminPermission;
import hu.kalmancheysandor.applications.dominions.apis.server.user.admin.service.authentication.AdminAuthenticationService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ai/otto/variant")
@Slf4j
public class OttoVariantController {
    @Autowired
    private OttoOperationServerVariantProxy ottoOperationServerVariantProxy;

    @Autowired
    private AdminAuthenticationService authenticationService;

    /// ///////////////////////////////////// ACCESS ////////////////////////////////////////////////////////////

    @GetMapping("/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    public OttoVariantAccessResponse access(@PathVariable("uuid") String uuid) {
        // Check permission
        authenticationService.assertHasAccessPermission(AdminPermission.generator().ai().otto().variant());

        return ottoOperationServerVariantProxy.access(uuid);
    }

    /// ///////////////////////////////////// LIST //////////////////////////////////////////////////////////////

    @GetMapping("/list")
    public List<OttoVariantItemResponse> listAll() {
        // Check permission
        authenticationService.assertHasAccessPermission(AdminPermission.generator().ai().otto().variant());

        System.out.println("LLLLLLLLIST");
        return ottoOperationServerVariantProxy.listAll();
    }

    /// ///////////////////////////////////// ADD ///////////////////////////////////////////////////////////////
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public OttoVariantCreateResponse add(@Valid @RequestBody OttoVariantCreateRequest request) {
        // Check permission
        authenticationService.assertHasAddPermission(AdminPermission.generator().ai().otto().variant());

        return ottoOperationServerVariantProxy.add(request);
    }
    @GetMapping("/add/options/evaluator")
    public List<OttoVariantEvaluatorOptionResponse> atAddListEvaluator() {
        // Check permission
        authenticationService.assertHasAddPermission(AdminPermission.generator().ai().otto().variant());

        return ottoOperationServerVariantProxy.atAddListEvaluator();
    }


    /// ///////////////////////////////////// EDIT ///////////////////////////////////////////////////////////////

    @PostMapping("/{uuid}/edit")
    @ResponseStatus(HttpStatus.OK)
    public OttoVariantUpdateResponse edit(@PathVariable("uuid") String uuid, @Valid @RequestBody OttoVariantUpdateRequest request) {
        // Check permission
        authenticationService.assertHasEditPermission(AdminPermission.generator().ai().otto().variant());

        return ottoOperationServerVariantProxy.edit(uuid, request);
    }

    @GetMapping("/{uuid}/edit/options/evaluator")
    public List<OttoVariantEvaluatorOptionResponse> atEditListEvaluator(@PathVariable("uuid") String uuid) {
        // Check permission
        authenticationService.assertHasEditPermission(AdminPermission.generator().ai().otto().variant());

        return ottoOperationServerVariantProxy.atEditListEvaluator(uuid);
    }
    /// ///////////////////////////////////// DELETE /////////////////////////////////////////////////////////////

    @DeleteMapping("/{uuid}/delete")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("uuid") String uuid) {
        // Check permission
        authenticationService.assertHasDeletePermission(AdminPermission.generator().ai().otto().variant());

        ottoOperationServerVariantProxy.delete(uuid);
    }

    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@Valid @RequestBody OttoVariantDeleteRequest request) {
        // Check permission
        authenticationService.assertHasDeletePermission(AdminPermission.generator().ai().otto().variant());

        ottoOperationServerVariantProxy.delete(request);
    }

}
