package hu.kalmancheysandor.applications.dominions.servers.admin.controller.training.hugo;


import hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.dto.variant.*;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.dto.variant.option.HugoVariantHeuristicOptionResponse;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.proxy.HugoOperationServerVariantProxy;
import hu.kalmancheysandor.applications.dominions.apis.server.user.admin.AdminPermission;
import hu.kalmancheysandor.applications.dominions.apis.server.user.admin.service.authentication.AdminAuthenticationService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ai/hugo/variant")
@Slf4j
public class HugoVariantController {
    @Autowired
    private HugoOperationServerVariantProxy hugoOperationServerVariantProxy;

    @Autowired
    private AdminAuthenticationService authenticationService;

    /// ///////////////////////////////////// ACCESS ////////////////////////////////////////////////////////////

    @GetMapping("/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    public HugoVariantAccessResponse access(@PathVariable("uuid") String uuid) {
        // Check permission
        authenticationService.assertHasAccessPermission(AdminPermission.generator().ai().hugo().variant());

        return hugoOperationServerVariantProxy.access(uuid);
    }

    /// ///////////////////////////////////// LIST //////////////////////////////////////////////////////////////

    @GetMapping("/list")
    public List<HugoVariantItemResponse> listAll() {
        // Check permission
        authenticationService.assertHasAccessPermission(AdminPermission.generator().ai().hugo().variant());

        System.out.println("LLLLLLLLIST");
        return hugoOperationServerVariantProxy.listAll();
    }

    /// ///////////////////////////////////// ADD ///////////////////////////////////////////////////////////////
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public HugoVariantCreateResponse add(@Valid @RequestBody HugoVariantCreateRequest request) {
        // Check permission
        authenticationService.assertHasAddPermission(AdminPermission.generator().ai().hugo().variant());

        return hugoOperationServerVariantProxy.add(request);
    }
    @GetMapping("/add/options/heuristic")
    public List<HugoVariantHeuristicOptionResponse> atAddListHeuristic() {
        // Check permission
        authenticationService.assertHasAddPermission(AdminPermission.generator().ai().hugo().variant());

        return hugoOperationServerVariantProxy.atAddListHeuristic();
    }


    /// ///////////////////////////////////// EDIT ///////////////////////////////////////////////////////////////

    @PostMapping("/{uuid}/edit")
    @ResponseStatus(HttpStatus.OK)
    public HugoVariantUpdateResponse edit(@PathVariable("uuid") String uuid, @Valid @RequestBody HugoVariantUpdateRequest request) {
        // Check permission
        authenticationService.assertHasEditPermission(AdminPermission.generator().ai().hugo().variant());

        return hugoOperationServerVariantProxy.edit(uuid, request);
    }

    @GetMapping("/{uuid}/edit/options/heuristic")
    public List<HugoVariantHeuristicOptionResponse> atEditListHeuristic(@PathVariable("uuid") String uuid) {
        // Check permission
        authenticationService.assertHasEditPermission(AdminPermission.generator().ai().hugo().variant());

        return hugoOperationServerVariantProxy.atEditListHeuristic(uuid);
    }
    /// ///////////////////////////////////// DELETE /////////////////////////////////////////////////////////////

    @DeleteMapping("/{uuid}/delete")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("uuid") String uuid) {
        // Check permission
        authenticationService.assertHasDeletePermission(AdminPermission.generator().ai().hugo().variant());

        hugoOperationServerVariantProxy.delete(uuid);
    }

    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@Valid @RequestBody HugoVariantDeleteRequest request) {
        // Check permission
        authenticationService.assertHasDeletePermission(AdminPermission.generator().ai().hugo().variant());

        hugoOperationServerVariantProxy.delete(request);
    }

}
