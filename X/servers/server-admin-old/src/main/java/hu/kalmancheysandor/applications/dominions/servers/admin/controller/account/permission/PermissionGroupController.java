package hu.kalmancheysandor.applications.dominions.servers.admin.controller.account.permission;

import hu.kalmancheysandor.applications.dominions.servers.admin.dto.entity.account.permission.*;
import hu.kalmancheysandor.applications.dominions.servers.admin.service.account.permission.PermissionGroupService;
import hu.kalmancheysandor.applications.dominions.servers.admin.configuration.security.permission.SecurityAuthorisation;
import hu.kalmancheysandor.applications.dominions.servers.admin.configuration.security.permission.SecurityPermission;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/data/account/permission")
public class PermissionGroupController {

    @Autowired
    private PermissionGroupService permissionGroupService;


    @GetMapping("/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    public PermissionGroupResponse access(@PathVariable("uuid") String uuid) {
        // Check permission
        SecurityAuthorisation.assertHasAccessPermission(SecurityPermission.generator().account().permissionGroup());

        return permissionGroupService.accessPermissionGroup(uuid);
    }

    @GetMapping("/list")
    public List<PermissionGroupResponse> list() {
        // Check permission
        SecurityAuthorisation.assertHasAccessPermission(SecurityPermission.generator().account().permissionGroup());

        return permissionGroupService.listAllPermissionGroup();
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public PermissionGroupCreateResponse add(@Valid @RequestBody PermissionGroupCreateRequest request) {
        // Check permission
        SecurityAuthorisation.assertHasAddPermission(SecurityPermission.generator().account().permissionGroup());

        return permissionGroupService.savePermissionGroup(request);
    }

    @PostMapping("/{uuid}/edit")
    @ResponseStatus(HttpStatus.OK)
    public PermissionGroupUpdateResponse update(@PathVariable("uuid") String uuid, @Valid @RequestBody PermissionGroupUpdateRequest request) {
        // Check permission
        SecurityAuthorisation.assertHasAddPermission(SecurityPermission.generator().account().permissionGroup());

        return permissionGroupService.updatePermissionGroup(uuid,request);
    }

    @DeleteMapping("/{uuid}/delete")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("uuid") String uuid) {
        // Check permission
        SecurityAuthorisation.assertHasAddPermission(SecurityPermission.generator().account().permissionGroup());

        permissionGroupService.deleteOnePermissionGroup(uuid);
    }

    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@Valid @RequestBody PermissionGroupDeleteRequest request) {
        // Check permission
        SecurityAuthorisation.assertHasDeletePermission(SecurityPermission.generator().account().permissionGroup());

        permissionGroupService.deleteMultiplePermissionGroup(request);
    }
}
