package hu.kalmancheysandor.applications.dominions.servers.admin.controller.account.site.user;


import hu.kalmancheysandor.applications.dominions.apis.server.user.admin.AdminPermission;
import hu.kalmancheysandor.applications.dominions.apis.server.user.site.dto.account.user.*;
import hu.kalmancheysandor.applications.dominions.apis.server.user.site.dto.account.user.option.SiteUserPermissionGroupOptionResponse;
import hu.kalmancheysandor.applications.dominions.apis.server.user.site.service.account.user.SiteUserService;
import hu.kalmancheysandor.applications.dominions.apis.server.user.site.service.authentication.SiteAuthenticationService;
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
@RequestMapping("/account/site/user")
public class SiteUserController {

    @Autowired
    private SiteUserService siteUserService;
    
    @Autowired
    private SiteAuthenticationService authenticationService;

    //////////////////////////////////////// ACCESS ////////////////////////////////////////////////////////////

    @GetMapping("/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    public SiteUserAccessResponse access(@PathVariable("uuid") String uuid) {
        // Check permission
        authenticationService.assertHasAccessPermission(AdminPermission.generator().account().site().user());

        return siteUserService.accessUser(uuid);
    }


    //////////////////////////////////////// LIST //////////////////////////////////////////////////////////////

    @GetMapping("/list")
    public List<SiteUserItemResponse> listAllUser() {
        // Check permission
        authenticationService.assertHasAccessPermission(AdminPermission.generator().account().site().user());

        return siteUserService.listAllUser();
    }

    //////////////////////////////////////// ADD ///////////////////////////////////////////////////////////////

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public SiteUserSaveResponse addUser(@Valid @RequestBody SiteUserSaveRequest request) {
        // Check permission
        authenticationService.assertHasAddPermission(AdminPermission.generator().account().site().user());

        return siteUserService.saveUser(request);
    }
    @GetMapping("/add/options/permission")
    public List<SiteUserPermissionGroupOptionResponse> atAddListBreed() {
        // Check permission
        authenticationService.assertHasAddPermission(AdminPermission.generator().account().site().user());

        return siteUserService.atSaveListPermissionGroup();
    }

    //////////////////////////////////////// EDIT ///////////////////////////////////////////////////////////////

    @PostMapping("/{uuid}/edit")
    @ResponseStatus(HttpStatus.OK)
    public SiteUserUpdateResponse updateUser(@PathVariable("uuid") String uuid, @Valid @RequestBody SiteUserUpdateRequest request) {
        // Check permission
        authenticationService.assertHasEditPermission(AdminPermission.generator().account().site().user());

        return siteUserService.updateUser(uuid,request);

    }


    @GetMapping("/{uuid}/edit/options/permission")
    public List<SiteUserPermissionGroupOptionResponse> atEditListBreed(@PathVariable("uuid") String uuid) {
        // Check permission
        authenticationService.assertHasEditPermission(AdminPermission.generator().account().site().user());

        return siteUserService.atUpdateListPermissionGroup(uuid);
    }

    //////////////////////////////////////// DELETE /////////////////////////////////////////////////////////////

    @DeleteMapping("/{uuid}/delete")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("uuid") String uuid) {
        // Check permission
        authenticationService.assertHasDeletePermission(AdminPermission.generator().account().site().user());

        siteUserService.deleteOneUser(uuid);
    }

    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@Valid @RequestBody SiteUserDeleteRequest request) {
        siteUserService.deleteMultipleUser(request);
    }

    @GetMapping("/{uuid}/photo")
    public ResponseEntity<Resource> accessUserPhoto(@PathVariable("uuid") String uuid, HttpServletRequest request) {

        // Check permission
        authenticationService.assertHasAccessPermission(AdminPermission.generator().account().site().user());

        FileHandler.Result result = siteUserService.accessUserPhoto(uuid);
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
