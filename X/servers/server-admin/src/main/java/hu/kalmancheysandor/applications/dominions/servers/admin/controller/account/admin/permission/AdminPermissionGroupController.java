package hu.kalmancheysandor.applications.dominions.servers.admin.controller.account.admin.permission;


import hu.kalmancheysandor.applications.dominions.apis.server.user.admin.dto.account.permission.*;
import hu.kalmancheysandor.applications.dominions.apis.server.user.admin.service.authentication.AdminAuthenticationService;
import hu.kalmancheysandor.applications.dominions.apis.server.user.admin.AdminPermission;
import hu.kalmancheysandor.applications.dominions.apis.server.user.admin.service.account.permission.AdminPermissionGroupService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/account/admin/permission")
public class AdminPermissionGroupController {

    @Autowired
    private AdminPermissionGroupService adminPermissionGroupService;

    @Autowired
    private AdminAuthenticationService authenticationService;
    
    
    @GetMapping("/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    public AdminPermissionGroupAccessResponse access(@PathVariable("uuid") String uuid) {
        // Check permission
        authenticationService.assertHasAccessPermission(AdminPermission.generator().account().admin().permissionGroup());

        return adminPermissionGroupService.accessPermissionGroup(uuid);
    }

    @GetMapping("/list")
    public List<AdminPermissionGroupItemResponse> list() {
        // Check permission
        authenticationService.assertHasAccessPermission(AdminPermission.generator().account().admin().permissionGroup());

        return adminPermissionGroupService.listAllPermissionGroup();
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public AdminPermissionGroupCreateResponse add(@Valid @RequestBody AdminPermissionGroupCreateRequest request) {
        // Check permission
        authenticationService.assertHasAddPermission(AdminPermission.generator().account().admin().permissionGroup());

        return adminPermissionGroupService.savePermissionGroup(request);
    }

    @PostMapping("/{uuid}/edit")
    @ResponseStatus(HttpStatus.OK)
    public AdminPermissionGroupUpdateResponse update(@PathVariable("uuid") String uuid, @Valid @RequestBody AdminPermissionGroupUpdateRequest request) {
        // Check permission
        authenticationService.assertHasAddPermission(AdminPermission.generator().account().admin().permissionGroup());

        return adminPermissionGroupService.updatePermissionGroup(uuid,request);
    }

    @DeleteMapping("/{uuid}/delete")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("uuid") String uuid) {
        // Check permission
        authenticationService.assertHasAddPermission(AdminPermission.generator().account().admin().permissionGroup());

        adminPermissionGroupService.deleteOnePermissionGroup(uuid);
    }

    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@Valid @RequestBody AdminPermissionGroupDeleteRequest request) {
        // Check permission
        authenticationService.assertHasDeletePermission(AdminPermission.generator().account().admin().permissionGroup());

        adminPermissionGroupService.deleteMultiplePermissionGroup(request);
    }
}

