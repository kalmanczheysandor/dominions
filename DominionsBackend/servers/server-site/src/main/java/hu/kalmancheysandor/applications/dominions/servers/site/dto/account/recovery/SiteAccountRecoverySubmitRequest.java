package hu.kalmancheysandor.applications.dominions.servers.site.dto.account.recovery;


import hu.kalmancheysandor.applications.dominions.apis.util.validate.ValidPassword;
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
public class SiteAccountRecoverySubmitRequest {
    private String verificationToken;
    @ValidPassword(minLength = 8,maxLength = 50, canBeEmpty = true)
    private String password;

    private String confirmPassword;
}
