package hu.kalmancheysandor.applications.dominions.servers.admin.controller.account.admin.user;


import hu.kalmancheysandor.applications.dominions.apis.server.user.admin.dto.account.user.*;
import hu.kalmancheysandor.applications.dominions.apis.server.user.admin.service.authentication.AdminAuthenticationService;

import hu.kalmancheysandor.applications.dominions.apis.server.user.admin.AdminPermission;
import hu.kalmancheysandor.applications.dominions.apis.server.user.admin.dto.account.user.option.AdminUserPermissionGroupOptionResponse;
import hu.kalmancheysandor.applications.dominions.apis.server.user.admin.service.account.user.AdminUserService;
import hu.kalmancheysandor.applications.dominions.apis.util.file.filehandler.FileHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;




@RestController
@RequestMapping("/account/admin/user")
public class AdminUserController {

    @Autowired
    private AdminUserService adminUserService;
    
    @Autowired
    private AdminAuthenticationService authenticationService;

    //////////////////////////////////////// ACCESS ////////////////////////////////////////////////////////////

    @GetMapping("/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    public AdminUserAccessResponse access(@PathVariable("uuid") String uuid) {
        // Check permission
        authenticationService.assertHasAccessPermission(AdminPermission.generator().account().admin().user());

        return adminUserService.accessUser(uuid);
    }


    //////////////////////////////////////// LIST //////////////////////////////////////////////////////////////

    @GetMapping("/list")
    public List<AdminUserItemResponse> listAllUser() {
        // Check permission
        authenticationService.assertHasAccessPermission(AdminPermission.generator().account().admin().user());

        return adminUserService.listAllUser();
    }

    //////////////////////////////////////// ADD ///////////////////////////////////////////////////////////////

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public AdminUserSaveResponse addUser(@Valid @RequestBody AdminUserSaveRequest request) {
        // Check permission
        authenticationService.assertHasAddPermission(AdminPermission.generator().account().admin().user());

        return adminUserService.saveUser(request);
    }
    @GetMapping("/add/options/permission")
    public List<AdminUserPermissionGroupOptionResponse> atAddListBreed() {
        // Check permission
        authenticationService.assertHasAddPermission(AdminPermission.generator().account().admin().user());

        return adminUserService.atSaveListPermissionGroup();
    }

    //////////////////////////////////////// EDIT ///////////////////////////////////////////////////////////////

    @PostMapping("/{uuid}/edit")
    @ResponseStatus(HttpStatus.OK)
    public AdminUserUpdateResponse updateUser(@PathVariable("uuid") String uuid, @Valid @RequestBody AdminUserUpdateRequest request) {
        // Check permission
        authenticationService.assertHasEditPermission(AdminPermission.generator().account().admin().user());

        return adminUserService.updateUser(uuid,request);

    }


    @GetMapping("/{uuid}/edit/options/permission")
    public List<AdminUserPermissionGroupOptionResponse> atEditListBreed(@PathVariable("uuid") String uuid) {
        // Check permission
        authenticationService.assertHasEditPermission(AdminPermission.generator().account().admin().user());

        return adminUserService.atUpdateListPermissionGroup(uuid);
    }

    //////////////////////////////////////// DELETE /////////////////////////////////////////////////////////////

    @DeleteMapping("/{uuid}/delete")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("uuid") String uuid) {
        // Check permission
        authenticationService.assertHasDeletePermission(AdminPermission.generator().account().admin().user());

        adminUserService.deleteOneUser(uuid);
    }

    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@Valid @RequestBody AdminUserDeleteRequest request) {
        // Check permission
        authenticationService.assertHasDeletePermission(AdminPermission.generator().account().admin().user());

        adminUserService.deleteMultipleUser(request);
    }



    @GetMapping("/{uuid}/photo")
    public ResponseEntity<Resource> accessUserPhoto(@PathVariable("uuid") String uuid, HttpServletRequest request) {

        // Check permission
        authenticationService.assertHasAccessPermission(AdminPermission.generator().account().admin().user());

        FileHandler.Result result = adminUserService.accessUserPhoto(uuid);
        if (result == null) {
            return ResponseEntity.noContent().build();
        }

        Resource resource = result.getResource();
        String etag = result.getEtag();
        long lastModified = result.getLastModified();

        // Response: If the file has not been changed, the cached version will be used on the frontend
        if (request.getHeader(HttpHeaders.IF_NONE_MATCH) != null && etag.equals(request.getHeader(HttpHeaders.IF_NONE_MATCH))) {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }
        if (request.getHeader(HttpHeaders.IF_MODIFIED_SINCE) != null && lastModified <= Long.parseLong(request.getHeader(HttpHeaders.IF_MODIFIED_SINCE))) {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }

        // Response: If the file has been changed or has not been cached
        return ResponseEntity.ok()
                .eTag(etag)
                .lastModified(lastModified)
                .contentType(MediaType.IMAGE_JPEG)
                .body(resource);

    }



}
