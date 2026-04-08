package hu.kalmancheysandor.applications.dominions.apis.server.mailing.shared.dto;

import hu.kalmancheysandor.applications.dominions.apis.server.mailing.shared.etc.MailDeliveryData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SiteAccountSignUpVerificationMailEnqueueRequest {
    private MailDeliveryData mailDeliveryData;
    private String verificationToken;
    private String expirationTime;
    private String userName;
}
