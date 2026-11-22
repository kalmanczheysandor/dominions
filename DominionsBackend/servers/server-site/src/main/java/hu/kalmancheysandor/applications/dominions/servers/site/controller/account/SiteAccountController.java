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



@RestController
@RequestMapping("/account")
public class SiteAccountController {

    @Autowired
    private SiteAccountService siteAccountService;
    
    @Autowired
    private SiteAuthenticationService authenticationService;


    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public SiteAccountSignUpResponse signUp(@Valid @RequestBody SiteAccountSignUpRequest request) {
        return siteAccountService.signUp(request);
    }

    @PostMapping("/signup/verification")
    @ResponseStatus(HttpStatus.OK)
    public SiteAccountSignUpVerificationResponse signUpVerification(@Valid @RequestBody SiteAccountSignUpVerificationRequest request) {
        return siteAccountService.signUpVerification(request);
    }




    @PostMapping("/recovery")
    @ResponseStatus(HttpStatus.OK)
    public SiteAccountSignUpResponse recovery(@Valid @RequestBody SiteAccountRecoveryRequest request) {
        return siteAccountService.recovery(request);
    }


    @PostMapping("/recovery/submit")
    @ResponseStatus(HttpStatus.OK)
    public SiteAccountRecoverySubmitResponse recoverySubmit(@Valid @RequestBody SiteAccountRecoverySubmitRequest request) {
        return siteAccountService.recoverySubmit(request);
    }


}
