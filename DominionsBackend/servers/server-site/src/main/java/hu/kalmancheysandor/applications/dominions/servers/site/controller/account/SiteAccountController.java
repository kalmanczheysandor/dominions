package hu.kalmancheysandor.applications.dominions.servers.site.controller.account;


import hu.kalmancheysandor.applications.dominions.apis.server.user.site.service.authentication.SiteAuthenticationService;
import hu.kalmancheysandor.applications.dominions.servers.site.dto.account.recovery.*;
import hu.kalmancheysandor.applications.dominions.servers.site.dto.account.signup.SiteAccountSignUpRequest;
import hu.kalmancheysandor.applications.dominions.servers.site.dto.account.signup.SiteAccountSignUpResponse;
import hu.kalmancheysandor.applications.dominions.servers.site.dto.account.signup.SiteAccountSignUpVerificationRequest;
import hu.kalmancheysandor.applications.dominions.servers.site.dto.account.signup.SiteAccountSignUpVerificationResponse;
import hu.kalmancheysandor.applications.dominions.servers.site.service.account.SiteAccountService;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RestController
@RequestMapping("/account")
public class SiteAccountController {

    @Autowired
    private SiteAccountService siteAccountService;
    
    @Autowired
    private SiteAuthenticationService authenticationService;

    private static final Logger log = LoggerFactory.getLogger(SiteAccountController.class);

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public SiteAccountSignUpResponse signUp(@Valid @RequestBody SiteAccountSignUpRequest request) {
        // Check permission
//        authenticationService.assertHasAddPermission(SitePermission.generator().account().signUp());

        return siteAccountService.signUp(request);
    }

    @PostMapping("/signup/verification")
    @ResponseStatus(HttpStatus.OK)
    public SiteAccountSignUpVerificationResponse signUpVerification(@Valid @RequestBody SiteAccountSignUpVerificationRequest request) {
        // Check permission
//        authenticationService.assertHasAddPermission(SitePermission.generator().account().signUp());

        return siteAccountService.signUpVerification(request);
    }




    @PostMapping("/recovery")
    @ResponseStatus(HttpStatus.OK)
    public SiteAccountSignUpResponse recovery(@Valid @RequestBody SiteAccountRecoveryRequest request) {
        // Check permission
//        authenticationService.assertHasAddPermission(SitePermission.generator().account().signUp());



        log.info("[RECOVERY] Login started");

        log.warn("[RECOVERY] Invalid password");

        log.error("[RECOVERY] Authentication failed");

        return siteAccountService.recovery(request);
    }

//    @PostMapping("/recovery/verification")
//    @ResponseStatus(HttpStatus.OK)
//    public SiteAccountRecoveryVerificationResponse recoveryVerification(@Valid @RequestBody SiteAccountRecoveryVerificationRequest request) {
//        // Check permission
////        authenticationService.assertHasAddPermission(SitePermission.generator().account().signUp());
//
//        return siteAccountService.recoveryVerification(request);
//    }

    @PostMapping("/recovery/submit")
    @ResponseStatus(HttpStatus.OK)
    public SiteAccountRecoverySubmitResponse recoverySubmit(@Valid @RequestBody SiteAccountRecoverySubmitRequest request) {
        // Check permission
//        authenticationService.assertHasAddPermission(SitePermission.generator().account().signUp());

        return siteAccountService.recoverySubmit(request);
    }

//
//
//
//    @GetMapping("/{uuid}")
//    @ResponseStatus(HttpStatus.OK)
//    public SiteUserAccessResponse access(@PathVariable("uuid") String uuid) {
//        // Check permission
//        authenticationService.assertHasAccessPermission(SitePermission.generator().account().user());
//
//        return siteUserService.accessUser(uuid);
//    }
//
//
//    //////////////////////////////////////// LIST //////////////////////////////////////////////////////////////
//
//    @GetMapping("/list")
//    public List<SiteUserItemResponse> listAllUser() {
//        // Check permission
//        authenticationService.assertHasAccessPermission(SitePermission.generator().account().user());
//
//        return siteUserService.listAllUser();
//    }
//
//    //////////////////////////////////////// ADD ///////////////////////////////////////////////////////////////
//
//    @PostMapping("/register")
//    @ResponseStatus(HttpStatus.CREATED)
//    public SiteUserRegisterResponse registerUser(@Valid @RequestBody SiteUserRegisterRequest request) {
//        // Check permission
//        authenticationService.assertHasAddPermission(SitePermission.generator().account().user());
//
//        return siteUserService.registerUser(request);
//    }
//
//
//
//
////    @PostMapping("/add")
////    @ResponseStatus(HttpStatus.CREATED)
////    public SiteUserCreateResponse addUser(@Valid @RequestBody SiteUserCreateRequest request) {
////        // Check permission
////        authenticationService.assertHasAddPermission(SitePermission.generator().account().user());
////
////        return siteUserService.saveUser(request);
////    }
//
//    //////////////////////////////////////// EDIT ///////////////////////////////////////////////////////////////
//
//    @PostMapping("/{uuid}/edit")
//    @ResponseStatus(HttpStatus.OK)
//    public SiteUserUpdateResponse updateUser(@PathVariable("uuid") String uuid, @Valid @RequestBody SiteUserUpdateRequest request) {
//        // Check permission
//        authenticationService.assertHasEditPermission(SitePermission.generator().account().user());
//
//        return siteUserService.updateUser(uuid,request);
//    }
//
//
//    //////////////////////////////////////// DELETE /////////////////////////////////////////////////////////////
//
//    @DeleteMapping("/{uuid}/delete")
//    @ResponseStatus(HttpStatus.OK)
//    public void delete(@PathVariable("uuid") String uuid) {
//        // Check permission
//        authenticationService.assertHasDeletePermission(SitePermission.generator().account().user());
//
//        siteUserService.deleteOneUser(uuid);
//    }
//
//    @DeleteMapping("/delete")
//    @ResponseStatus(HttpStatus.OK)
//    public void delete(@Valid @RequestBody SiteUserDeleteRequest request) {
//        siteUserService.deleteMultipleUser(request);
//    }

}
