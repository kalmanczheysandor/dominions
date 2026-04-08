package hu.kalmancheysandor.applications.dominions.servers.site.controller.account.settings;


import hu.kalmancheysandor.applications.dominions.apis.server.common.component.config.ApplicationConfig;
import hu.kalmancheysandor.applications.dominions.apis.server.user.site.SitePermission;
import hu.kalmancheysandor.applications.dominions.apis.server.user.site.service.authentication.SiteAuthenticationService;
import hu.kalmancheysandor.applications.dominions.apis.util.file.filehandler.FileHandler;
import hu.kalmancheysandor.applications.dominions.servers.site.dto.account.settings.SiteAccountSettingsCredentialPasswordAccessResponse;
import hu.kalmancheysandor.applications.dominions.servers.site.dto.account.settings.SiteAccountSettingsCredentialPasswordUpdateRequest;
import hu.kalmancheysandor.applications.dominions.servers.site.dto.account.settings.SiteAccountSettingsProfileDetailsAccessResponse;
import hu.kalmancheysandor.applications.dominions.servers.site.dto.account.settings.SiteAccountSettingsProfileDetailsUpdateRequest;
import hu.kalmancheysandor.applications.dominions.servers.site.service.account.settings.SiteAccountSettingsService;
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
@RequestMapping("/account/settings")
public class SiteAccountSettingsController {

    @Autowired
    private SiteAccountSettingsService siteSettingsService;

    @Autowired
    private ApplicationConfig applicationConfig;

    @Autowired
    private SiteAuthenticationService authenticationService;

    /// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /// /////  PROFILE  ////////////////////////////////////////////////////////////////////////////////////////////////
    /// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @GetMapping("/profile")
    @ResponseStatus(HttpStatus.OK)
    public SiteAccountSettingsProfileDetailsAccessResponse accessAccountProfileDetails() {
        // Check permission
        authenticationService.assertHasAccessPermission(SitePermission.generator().account().settings());
        System.out.println("XXX-A1");
        return siteSettingsService.accessAccountProfileDetails();
    }

    @PostMapping("/profile/edit")
    @ResponseStatus(HttpStatus.OK)
    public void updateAccountProfileDetails(@Valid @RequestBody SiteAccountSettingsProfileDetailsUpdateRequest request) {
        // Check permission
        authenticationService.assertHasEditPermission(SitePermission.generator().account().settings());

        siteSettingsService.updateAccountProfileDetails(request);
    }

    @GetMapping("/profile/photo")
    public ResponseEntity<Resource> accessAccountProfilePhoto(HttpServletRequest request) {

        // Check permission
        authenticationService.assertHasAccessPermission(SitePermission.generator().account().settings());

        FileHandler.Result result = siteSettingsService.accessAccountProfilePhoto();
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
    public SiteAccountSettingsCredentialPasswordAccessResponse accessAccountCredentialPassword() {
        // Check permission
        authenticationService.assertHasAccessPermission(SitePermission.generator().account().settings());

        return siteSettingsService.accessAccountCredentialPassword();
    }


    @PostMapping("/credential/password/edit")
    @ResponseStatus(HttpStatus.OK)
    public void updateProfileCredentialPassword(@Valid @RequestBody SiteAccountSettingsCredentialPasswordUpdateRequest request) {
        // Check permission
        authenticationService.assertHasEditPermission(SitePermission.generator().account().settings());

        siteSettingsService.updateAccountCredentialPassword(request);
    }


}
