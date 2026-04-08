package hu.kalmancheysandor.applications.dominions.servers.site.dto.account.signup;


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
public class SiteAccountSignUpRequest {
    private String identifier;
    private String name;
    private String password;
    private String confirmPassword;
}
