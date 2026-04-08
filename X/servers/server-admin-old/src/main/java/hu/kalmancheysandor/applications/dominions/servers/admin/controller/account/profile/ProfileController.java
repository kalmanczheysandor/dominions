package hu.kalmancheysandor.applications.dominions.servers.admin.controller.account.profile;

import hu.kalmancheysandor.applications.dominions.servers.admin.dto.entity.account.profile.ProfileCredentialPasswordAccessResponse;
import hu.kalmancheysandor.applications.dominions.servers.admin.dto.entity.account.profile.ProfileCredentialPasswordUpdateRequest;
import hu.kalmancheysandor.applications.dominions.servers.admin.dto.entity.account.profile.ProfileDetailsAccessResponse;
import hu.kalmancheysandor.applications.dominions.servers.admin.dto.entity.account.profile.ProfileDetailsUpdateRequest;
import hu.kalmancheysandor.applications.dominions.servers.admin.service.account.profile.ProfileService;
import hu.kalmancheysandor.applications.dominions.servers.admin.configuration.security.permission.SecurityAuthorisation;
import hu.kalmancheysandor.applications.dominions.servers.admin.configuration.security.permission.SecurityPermission;
import hu.kalmancheysandor.applications.dominions.servers.admin.utils.filehandler.FileHandler;
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
@RequestMapping("/data/account/profile")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    //////////////////////////////////////// ACCESS ////////////////////////////////////////////////////////////






    @GetMapping("/details")
    @ResponseStatus(HttpStatus.OK)
    public ProfileDetailsAccessResponse accessProfileDetails() {
        // Check permission
        SecurityAuthorisation.assertHasAccessPermission(SecurityPermission.generator().account().profile());

        return profileService.accessProfileDetails();
    }

    @PostMapping("/details/edit")
    @ResponseStatus(HttpStatus.OK)
    public void updateProfileDetails(@Valid @RequestBody ProfileDetailsUpdateRequest request) {
        // Check permission
        SecurityAuthorisation.assertHasEditPermission(SecurityPermission.generator().account().profile());

        profileService.updateProfileDetails(request);
    }

    @GetMapping("/details/image/main")
    public ResponseEntity<Resource> image(HttpServletRequest request) {

        // Check permission
        SecurityAuthorisation.assertHasAccessPermission(SecurityPermission.generator().account().profile());


        FileHandler.Result result = profileService.image();
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






    @GetMapping("/credential/password")
    @ResponseStatus(HttpStatus.OK)
    public ProfileCredentialPasswordAccessResponse accessProfileCredentialPassword() {
        // Check permission
        SecurityAuthorisation.assertHasAccessPermission(SecurityPermission.generator().account().profile());

        return profileService.accessProfileCredentialPassword();
    }



    @PostMapping("/credential/password/edit")
    @ResponseStatus(HttpStatus.OK)
    public void updateProfileCredentialPassword(@Valid @RequestBody ProfileCredentialPasswordUpdateRequest request) {
        // Check permission
        SecurityAuthorisation.assertHasEditPermission(SecurityPermission.generator().account().profile());

        profileService.updateProfileCredentialPassword(request);
    }















}


    //////////////////////////////////////// LIST //////////////////////////////////////////////////////////////

//
//    @GetMapping("/list")
//    public List<ProfileResponse> listAllProfile() {
//        // Check permission
//        SecurityAuthorisation.assertHasAccessPermission(SecurityPermission.generator().account().profile());
//
//        return profileService.listAllProfile();
//    }
//
//    //////////////////////////////////////// ADD ///////////////////////////////////////////////////////////////
//
//
//
//    //////////////////////////////////////// EDIT ///////////////////////////////////////////////////////////////
//
//    @PostMapping("edit")
//    @ResponseStatus(HttpStatus.OK)
//    public ProfileUpdateResponse updateProfile(@Valid @RequestBody ProfileUpdateRequest request) {
//        // Check permission
//        SecurityAuthorisation.assertHasEditPermission(SecurityPermission.generator().account().profile());
//
//        return profileService.updateProfile(uuid,request);
//
//    }
//}
//
