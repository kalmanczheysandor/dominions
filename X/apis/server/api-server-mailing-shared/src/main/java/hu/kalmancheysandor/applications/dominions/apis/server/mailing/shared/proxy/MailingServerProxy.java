package hu.kalmancheysandor.applications.dominions.apis.server.mailing.shared.proxy;

import hu.kalmancheysandor.applications.dominions.apis.server.mailing.shared.dto.SiteAccountRecoveryVerificationMailEnqueueRequest;
import hu.kalmancheysandor.applications.dominions.apis.server.mailing.shared.dto.SiteAccountSignUpVerificationMailEnqueueRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@FeignClient(name = "server-mailing")
public interface MailingServerProxy {

    @PostMapping("/site/account/signup/verification/enqueue")
    @ResponseStatus(HttpStatus.OK)
    public void enqueueSiteAccountSignUpVerificationMail(@RequestBody SiteAccountSignUpVerificationMailEnqueueRequest request);


    @PostMapping("/site/account/recovery/verification/enqueue")
    @ResponseStatus(HttpStatus.OK)
    public void enqueueSiteAccountRecoveryVerificationMail(@RequestBody SiteAccountRecoveryVerificationMailEnqueueRequest request);

}