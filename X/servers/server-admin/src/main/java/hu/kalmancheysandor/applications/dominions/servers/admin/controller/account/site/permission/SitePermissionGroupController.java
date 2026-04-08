package hu.kalmancheysandor.applications.dominions.servers.admin.controller.account.site.permission;


import hu.kalmancheysandor.applications.dominions.apis.server.user.admin.AdminPermission;
import hu.kalmancheysandor.applications.dominions.apis.server.user.site.SitePermission;
import hu.kalmancheysandor.applications.dominions.apis.server.user.site.dto.account.permission.*;
import hu.kalmancheysandor.applications.dominions.apis.server.user.site.service.account.permission.SitePermissionGroupService;
import hu.kalmancheysandor.applications.dominions.apis.server.user.site.service.authentication.SiteAuthenticationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/account/site/permission")
public class SitePermissionGroupController {

    @Autowired
    private SitePermissionGroupService sitePermissionGroupService;

    @Autowired
    private SiteAuthenticationService authenticationService;
    
    
    @GetMapping("/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    public SitePermissionGroupAccessResponse access(@PathVariable("uuid") String uuid) {
        // Check permission
        authenticationService.assertHasAccessPermission(AdminPermission.generator().account().site().permissionGroup());

        return sitePermissionGroupService.accessPermissionGroup(uuid);
    }

    @GetMapping("/list")
    public List<SitePermissionGroupItemResponse> list() {
        // Check permission
        authenticationService.assertHasAccessPermission(AdminPermission.generator().account().site().permissionGroup());

        return sitePermissionGroupService.listAllPermissionGroup();
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public SitePermissionGroupCreateResponse add(@Valid @RequestBody SitePermissionGroupCreateRequest request) {
        // Check permission
        authenticationService.assertHasAddPermission(AdminPermission.generator().account().site().permissionGroup());

        return sitePermissionGroupService.savePermissionGroup(request);
    }

    @PostMapping("/{uuid}/edit")
    @ResponseStatus(HttpStatus.OK)
    public SitePermissionGroupUpdateResponse update(@PathVariable("uuid") String uuid, @Valid @RequestBody SitePermissionGroupUpdateRequest request) {
        // Check permission
        authenticationService.assertHasAddPermission(AdminPermission.generator().account().site().permissionGroup());

        return sitePermissionGroupService.updatePermissionGroup(uuid,request);
    }

    @DeleteMapping("/{uuid}/delete")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("uuid") String uuid) {
        // Check permission
        authenticationService.assertHasAddPermission(AdminPermission.generator().account().site().permissionGroup());

        sitePermissionGroupService.deleteOnePermissionGroup(uuid);
    }

    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@Valid @RequestBody SitePermissionGroupDeleteRequest request) {
        // Check permission
        authenticationService.assertHasDeletePermission(AdminPermission.generator().account().site().permissionGroup());

        sitePermissionGroupService.deleteMultiplePermissionGroup(request);
    }
}

