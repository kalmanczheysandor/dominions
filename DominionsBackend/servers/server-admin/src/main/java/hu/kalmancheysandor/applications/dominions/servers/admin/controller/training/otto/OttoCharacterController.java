package hu.kalmancheysandor.applications.dominions.servers.admin.controller.training.otto;


import hu.kalmancheysandor.applications.dominions.apis.server.ai.otto.shared.dto.character.*;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.otto.shared.dto.character.option.OttoCharacterVariantOptionResponse;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.otto.shared.proxy.OttoOperationServerCharacterProxy;
import hu.kalmancheysandor.applications.dominions.apis.server.user.admin.AdminPermission;
import hu.kalmancheysandor.applications.dominions.apis.server.user.admin.service.authentication.AdminAuthenticationService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ai/otto/character")
@Slf4j
public class OttoCharacterController {
    @Autowired
    private OttoOperationServerCharacterProxy ottoOperationServerCharacterProxy;

    @Autowired
    private AdminAuthenticationService authenticationService;

    /// ///////////////////////////////////// ACCESS ////////////////////////////////////////////////////////////

    @GetMapping("/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    public OttoCharacterAccessResponse access(@PathVariable("uuid") String uuid) {
        // Check permission
        authenticationService.assertHasAccessPermission(AdminPermission.generator().ai().otto().character());

        return ottoOperationServerCharacterProxy.access(uuid);
    }

    /// ///////////////////////////////////// LIST //////////////////////////////////////////////////////////////

    @GetMapping("/list")
    public List<OttoCharacterItemResponse> listAll() {
        // Check permission
        authenticationService.assertHasAccessPermission(AdminPermission.generator().ai().otto().character());

        System.out.println("LLLLLLLLIST");
        return ottoOperationServerCharacterProxy.listAll();
    }

    /// ///////////////////////////////////// ADD ///////////////////////////////////////////////////////////////
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public OttoCharacterCreateResponse add(@Valid @RequestBody OttoCharacterCreateRequest request) {
        // Check permission
        authenticationService.assertHasAddPermission(AdminPermission.generator().ai().otto().character());

        return ottoOperationServerCharacterProxy.add(request);
    }

    @GetMapping("/add/options/variant")
    public List<OttoCharacterVariantOptionResponse> atAddListVariant() {
        // Check permission
        authenticationService.assertHasAddPermission(AdminPermission.generator().ai().otto().character());

        return ottoOperationServerCharacterProxy.atAddListVariant();
    }

    /// ///////////////////////////////////// EDIT ///////////////////////////////////////////////////////////////

    @PostMapping("/{uuid}/edit")
    @ResponseStatus(HttpStatus.OK)
    public OttoCharacterUpdateResponse edit(@PathVariable("uuid") String uuid, @Valid @RequestBody OttoCharacterUpdateRequest request) {
        // Check permission
        authenticationService.assertHasEditPermission(AdminPermission.generator().ai().otto().character());

        return ottoOperationServerCharacterProxy.edit(uuid, request);
    }

    @GetMapping("/{uuid}/edit/options/variant")
    public List<OttoCharacterVariantOptionResponse> atEditListVariant(@PathVariable("uuid") String uuid) {
        // Check permission
        authenticationService.assertHasEditPermission(AdminPermission.generator().ai().otto().character());

        return ottoOperationServerCharacterProxy.atEditListVariant(uuid);
    }

    /// ///////////////////////////////////// DELETE /////////////////////////////////////////////////////////////

    @DeleteMapping("/{uuid}/delete")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("uuid") String uuid) {
        // Check permission
        authenticationService.assertHasDeletePermission(AdminPermission.generator().ai().otto().character());

        ottoOperationServerCharacterProxy.delete(uuid);
    }

    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@Valid @RequestBody OttoCharacterDeleteRequest request) {
        // Check permission
        authenticationService.assertHasDeletePermission(AdminPermission.generator().ai().otto().character());

        ottoOperationServerCharacterProxy.delete(request);
    }

}
