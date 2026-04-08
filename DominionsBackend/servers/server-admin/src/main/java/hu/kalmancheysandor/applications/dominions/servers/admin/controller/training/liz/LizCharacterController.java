package hu.kalmancheysandor.applications.dominions.servers.admin.controller.training.liz;


import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.dto.character.*;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.dto.character.option.LizCharacterVariantOptionResponse;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.proxy.LizOperationServerCharacterProxy;
import hu.kalmancheysandor.applications.dominions.apis.server.user.admin.AdminPermission;
import hu.kalmancheysandor.applications.dominions.apis.server.user.admin.service.authentication.AdminAuthenticationService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ai/liz/character")
@Slf4j
public class LizCharacterController {
    @Autowired
    private LizOperationServerCharacterProxy lizOperationServerCharacterProxy;

    @Autowired
    private AdminAuthenticationService authenticationService;

    /// ///////////////////////////////////// ACCESS ////////////////////////////////////////////////////////////

    @GetMapping("/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    public LizCharacterAccessResponse access(@PathVariable("uuid") String uuid) {
        // Check permission
        authenticationService.assertHasAccessPermission(AdminPermission.generator().ai().liz().character());

        return lizOperationServerCharacterProxy.access(uuid);
    }

    /// ///////////////////////////////////// LIST //////////////////////////////////////////////////////////////

    @GetMapping("/list")
    public List<LizCharacterItemResponse> listAll() {
        // Check permission
        authenticationService.assertHasAccessPermission(AdminPermission.generator().ai().liz().character());

        System.out.println("LLLLLLLLIST");
        return lizOperationServerCharacterProxy.listAll();
    }

    /// ///////////////////////////////////// ADD ///////////////////////////////////////////////////////////////
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public LizCharacterCreateResponse add(@Valid @RequestBody LizCharacterCreateRequest request) {
        // Check permission
        authenticationService.assertHasAddPermission(AdminPermission.generator().ai().liz().character());

        return lizOperationServerCharacterProxy.add(request);
    }

    @GetMapping("/add/options/variant")
    public List<LizCharacterVariantOptionResponse> atAddListVariant() {
        // Check permission
        authenticationService.assertHasAddPermission(AdminPermission.generator().ai().liz().character());

        return lizOperationServerCharacterProxy.atAddListVariant();
    }

    /// ///////////////////////////////////// EDIT ///////////////////////////////////////////////////////////////

    @PostMapping("/{uuid}/edit")
    @ResponseStatus(HttpStatus.OK)
    public LizCharacterUpdateResponse edit(@PathVariable("uuid") String uuid, @Valid @RequestBody LizCharacterUpdateRequest request) {
        // Check permission
        authenticationService.assertHasEditPermission(AdminPermission.generator().ai().liz().character());

        return lizOperationServerCharacterProxy.edit(uuid, request);
    }

    @GetMapping("/{uuid}/edit/options/variant")
    public List<LizCharacterVariantOptionResponse> atEditListVariant(@PathVariable("uuid") String uuid) {
        // Check permission
        authenticationService.assertHasEditPermission(AdminPermission.generator().ai().liz().character());

        return lizOperationServerCharacterProxy.atEditListVariant(uuid);
    }

    /// ///////////////////////////////////// DELETE /////////////////////////////////////////////////////////////

    @DeleteMapping("/{uuid}/delete")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("uuid") String uuid) {
        // Check permission
        authenticationService.assertHasDeletePermission(AdminPermission.generator().ai().liz().character());

        lizOperationServerCharacterProxy.delete(uuid);
    }

    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@Valid @RequestBody LizCharacterDeleteRequest request) {
        // Check permission
        authenticationService.assertHasDeletePermission(AdminPermission.generator().ai().liz().character());

        lizOperationServerCharacterProxy.delete(request);
    }

}
