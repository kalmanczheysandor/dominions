package hu.kalmancheysandor.applications.dominions.servers.admin.controller.training.liz;


import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.dto.character.option.LizCharacterVariantOptionResponse;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.dto.variant.*;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.dto.variant.option.LizVariantConceptOptionResponse;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.proxy.LizOperationServerVariantProxy;
import hu.kalmancheysandor.applications.dominions.apis.server.user.admin.AdminPermission;
import hu.kalmancheysandor.applications.dominions.apis.server.user.admin.service.authentication.AdminAuthenticationService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ai/liz/variant")
@Slf4j
public class LizVariantController {
    @Autowired
    private LizOperationServerVariantProxy lizOperationServerVariantProxy;

    @Autowired
    private AdminAuthenticationService authenticationService;

    /// ///////////////////////////////////// ACCESS ////////////////////////////////////////////////////////////

    @GetMapping("/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    public LizVariantAccessResponse access(@PathVariable("uuid") String uuid) {
        // Check permission
        authenticationService.assertHasAccessPermission(AdminPermission.generator().ai().liz().variant());

        return lizOperationServerVariantProxy.access(uuid);
    }

    /// ///////////////////////////////////// LIST //////////////////////////////////////////////////////////////

    @GetMapping("/list")
    public List<LizVariantItemResponse> listAll() {
        // Check permission
        authenticationService.assertHasAccessPermission(AdminPermission.generator().ai().liz().variant());

        System.out.println("LLLLLLLLIST");
        return lizOperationServerVariantProxy.listAll();
    }

    /// ///////////////////////////////////// ADD ///////////////////////////////////////////////////////////////
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public LizVariantCreateResponse add(@Valid @RequestBody LizVariantCreateRequest request) {
        // Check permission
        authenticationService.assertHasAddPermission(AdminPermission.generator().ai().liz().variant());

        return lizOperationServerVariantProxy.add(request);
    }
    @GetMapping("/add/options/concept")
    public List<LizVariantConceptOptionResponse> atAddListConcept() {
        // Check permission
        authenticationService.assertHasAddPermission(AdminPermission.generator().ai().liz().variant());

        return lizOperationServerVariantProxy.atAddListConcept();
    }


    /// ///////////////////////////////////// EDIT ///////////////////////////////////////////////////////////////

    @PostMapping("/{uuid}/edit")
    @ResponseStatus(HttpStatus.OK)
    public LizVariantUpdateResponse edit(@PathVariable("uuid") String uuid, @Valid @RequestBody LizVariantUpdateRequest request) {
        // Check permission
        authenticationService.assertHasEditPermission(AdminPermission.generator().ai().liz().variant());

        return lizOperationServerVariantProxy.edit(uuid, request);
    }

    @GetMapping("/{uuid}/edit/options/concept")
    public List<LizVariantConceptOptionResponse> atEditListVariant(@PathVariable("uuid") String uuid) {
        // Check permission
        authenticationService.assertHasEditPermission(AdminPermission.generator().ai().liz().variant());

        return lizOperationServerVariantProxy.atEditListConcept(uuid);
    }
    /// ///////////////////////////////////// DELETE /////////////////////////////////////////////////////////////

    @DeleteMapping("/{uuid}/delete")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("uuid") String uuid) {
        // Check permission
        authenticationService.assertHasDeletePermission(AdminPermission.generator().ai().liz().variant());

        lizOperationServerVariantProxy.delete(uuid);
    }

    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@Valid @RequestBody LizVariantDeleteRequest request) {
        // Check permission
        authenticationService.assertHasDeletePermission(AdminPermission.generator().ai().liz().variant());

        lizOperationServerVariantProxy.delete(request);
    }

}
