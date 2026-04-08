package hu.kalmancheysandor.applications.dominions.servers.site.dto.account.recovery;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Validated
public class SiteAccountRecoveryVerificationRequest {
    private String verificationToken;
}
