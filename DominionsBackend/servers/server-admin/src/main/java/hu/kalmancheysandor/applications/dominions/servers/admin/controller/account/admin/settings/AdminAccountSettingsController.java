package hu.kalmancheysandor.applications.dominions.servers.admin.controller.account.admin.settings;

import hu.kalmancheysandor.applications.dominions.apis.server.common.component.config.ApplicationConfig;
import hu.kalmancheysandor.applications.dominions.apis.server.user.admin.AdminPermission;
import hu.kalmancheysandor.applications.dominions.apis.server.user.admin.service.authentication.AdminAuthenticationService;
import hu.kalmancheysandor.applications.dominions.apis.util.file.filehandler.FileHandler;
import hu.kalmancheysandor.applications.dominions.servers.admin.dto.account.admin.settings.AdminAccountSettingsCredentialPasswordAccessResponse;
import hu.kalmancheysandor.applications.dominions.servers.admin.dto.account.admin.settings.AdminAccountSettingsCredentialPasswordUpdateRequest;
import hu.kalmancheysandor.applications.dominions.servers.admin.dto.account.admin.settings.AdminAccountSettingsProfileDetailsAccessResponse;
import hu.kalmancheysandor.applications.dominions.servers.admin.dto.account.admin.settings.AdminAccountSettingsProfileDetailsUpdateRequest;
import hu.kalmancheysandor.applications.dominions.servers.admin.service.account.admin.settings.AdminAccountSettingsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account/admin/settings")
public class AdminAccountSettingsController {

    @Autowired
    private AdminAccountSettingsService adminSettingsService;
    
    @Autowired
    private AdminAuthenticationService authenticationService;

    /// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /// /////  PROFILE  ////////////////////////////////////////////////////////////////////////////////////////////////
    /// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @GetMapping("/profile")
    @ResponseStatus(HttpStatus.OK)
    public AdminAccountSettingsProfileDetailsAccessResponse accessAccountProfileDetails() {
        // Check permission
        authenticationService.assertHasAccessPermission(AdminPermission.generator().account().admin().settings());

        return adminSettingsService.accessAccountProfileDetails();
    }

    @PostMapping("/profile/edit")
    @ResponseStatus(HttpStatus.OK)
    public void updateAccountProfileDetails(@Valid @RequestBody AdminAccountSettingsProfileDetailsUpdateRequest request) {
        // Check permission
        authenticationService.assertHasEditPermission(AdminPermission.generator().account().admin().settings());

        adminSettingsService.updateAccountProfileDetails(request);
    }

    @GetMapping("/profile/photo")
    public ResponseEntity<Resource> accessAccountProfilePhoto(HttpServletRequest request) {

        // Check permission
        authenticationService.assertHasAccessPermission(AdminPermission.generator().account().admin().settings());

        FileHandler.Result result = adminSettingsService.accessAccountProfilePhoto();
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

    /// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /// /////  CREDENTIAL  /////////////////////////////////////////////////////////////////////////////////////////////
    /// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @GetMapping("/credential/password")
    @ResponseStatus(HttpStatus.OK)
    public AdminAccountSettingsCredentialPasswordAccessResponse accessAccountCredentialPassword() {
        // Check permission
        authenticationService.assertHasAccessPermission(AdminPermission.generator().account().admin().settings());

        return adminSettingsService.accessAccountCredentialPassword();
    }


    @PostMapping("/credential/password/edit")
    @ResponseStatus(HttpStatus.OK)
    public void updateProfileCredentialPassword(@Valid @RequestBody AdminAccountSettingsCredentialPasswordUpdateRequest request) {
        // Check permission
        authenticationService.assertHasEditPermission(AdminPermission.generator().account().admin().settings());

        adminSettingsService.updateAccountCredentialPassword(request);
    }


}
