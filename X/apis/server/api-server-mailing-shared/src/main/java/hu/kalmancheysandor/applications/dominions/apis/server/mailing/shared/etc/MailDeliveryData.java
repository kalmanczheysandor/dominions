package hu.kalmancheysandor.applications.dominions.apis.server.mailing.shared.etc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MailDeliveryData {
    private String senderEmail;
    private String senderName;
    private String recipientEmail;
    private String subject;
}
