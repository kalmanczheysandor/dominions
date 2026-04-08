package hu.kalmancheysandor.applications.dominions.servers.mailing.controller;


import hu.kalmancheysandor.applications.dominions.apis.server.mailing.shared.dto.SiteAccountRecoveryVerificationMailEnqueueRequest;
import hu.kalmancheysandor.applications.dominions.apis.server.mailing.shared.dto.SiteAccountSignUpVerificationMailEnqueueRequest;
import hu.kalmancheysandor.applications.dominions.servers.mailing.service.SiteAccountMailDeliveryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/site/account")
public class SiteAccountMailDeliveryController {
    @Autowired
    private SiteAccountMailDeliveryService siteAccountMailDeliveryService;

    @PostMapping("/signup/verification/enqueue")
    @ResponseStatus(HttpStatus.OK)
    public void enqueueSiteAccountSignUpVerificationMail(@Valid @RequestBody SiteAccountSignUpVerificationMailEnqueueRequest request) {
        siteAccountMailDeliveryService.enqueueSiteAccountSignUpVerificationMail(request);
    }


    @PostMapping("/recovery/verification/enqueue")
    @ResponseStatus(HttpStatus.OK)
    public void enqueueSiteAccountRecoveryVerificationMail(@Valid @RequestBody SiteAccountRecoveryVerificationMailEnqueueRequest request) {
        siteAccountMailDeliveryService.enqueueSiteAccountRecoveryVerificationMail(request);
    }
}
