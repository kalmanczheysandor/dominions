package hu.kalmancheysandor.applications.dominions.servers.admin.controller.training.hugo;


import hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.dto.character.*;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.dto.character.option.HugoCharacterVariantOptionResponse;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.proxy.HugoOperationServerCharacterProxy;
import hu.kalmancheysandor.applications.dominions.apis.server.user.admin.AdminPermission;
import hu.kalmancheysandor.applications.dominions.apis.server.user.admin.service.authentication.AdminAuthenticationService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ai/hugo/character")
@Slf4j
public class HugoCharacterController {
    @Autowired
    private HugoOperationServerCharacterProxy hugoOperationServerCharacterProxy;

    @Autowired
    private AdminAuthenticationService authenticationService;

    /// ///////////////////////////////////// ACCESS ////////////////////////////////////////////////////////////

    @GetMapping("/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    public HugoCharacterAccessResponse access(@PathVariable("uuid") String uuid) {
        // Check permission
        authenticationService.assertHasAccessPermission(AdminPermission.generator().ai().hugo().character());

        return hugoOperationServerCharacterProxy.access(uuid);
    }

    /// ///////////////////////////////////// LIST //////////////////////////////////////////////////////////////

    @GetMapping("/list")
    public List<HugoCharacterItemResponse> listAll() {
        // Check permission
        authenticationService.assertHasAccessPermission(AdminPermission.generator().ai().hugo().character());

        System.out.println("LLLLLLLLIST");
        return hugoOperationServerCharacterProxy.listAll();
    }

    /// ///////////////////////////////////// ADD ///////////////////////////////////////////////////////////////
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public HugoCharacterCreateResponse add(@Valid @RequestBody HugoCharacterCreateRequest request) {
        // Check permission
        authenticationService.assertHasAddPermission(AdminPermission.generator().ai().hugo().character());

        return hugoOperationServerCharacterProxy.add(request);
    }

    @GetMapping("/add/options/variant")
    public List<HugoCharacterVariantOptionResponse> atAddListVariant() {
        // Check permission
        authenticationService.assertHasAddPermission(AdminPermission.generator().ai().hugo().character());

        return hugoOperationServerCharacterProxy.atAddListVariant();
    }

    /// ///////////////////////////////////// EDIT ///////////////////////////////////////////////////////////////

    @PostMapping("/{uuid}/edit")
    @ResponseStatus(HttpStatus.OK)
    public HugoCharacterUpdateResponse edit(@PathVariable("uuid") String uuid, @Valid @RequestBody HugoCharacterUpdateRequest request) {
        // Check permission
        authenticationService.assertHasEditPermission(AdminPermission.generator().ai().hugo().character());

        return hugoOperationServerCharacterProxy.edit(uuid, request);
    }

    @GetMapping("/{uuid}/edit/options/variant")
    public List<HugoCharacterVariantOptionResponse> atEditListVariant(@PathVariable("uuid") String uuid) {
        // Check permission
        authenticationService.assertHasEditPermission(AdminPermission.generator().ai().hugo().character());

        return hugoOperationServerCharacterProxy.atEditListVariant(uuid);
    }

    /// ///////////////////////////////////// DELETE /////////////////////////////////////////////////////////////

    @DeleteMapping("/{uuid}/delete")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("uuid") String uuid) {
        // Check permission
        authenticationService.assertHasDeletePermission(AdminPermission.generator().ai().hugo().character());

        hugoOperationServerCharacterProxy.delete(uuid);
    }

    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@Valid @RequestBody HugoCharacterDeleteRequest request) {
        // Check permission
        authenticationService.assertHasDeletePermission(AdminPermission.generator().ai().hugo().character());

        hugoOperationServerCharacterProxy.delete(request);
    }

}
