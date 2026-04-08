package hu.kalmancheysandor.applications.dominions.servers.admin.dto.auth;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

@Data
@NoArgsConstructor
@Validated
public class LoginRequest {
//    @NotNull(message = "Field must not be null")
//    @Email(message="Field must be a in e-mail format")
//    @NotBlank(message = "Field must not be blank")
    private String username;

//    @ValidPassword(minLength = 8,maxLength = 50, canBeEmpty = false)
    private String password;
}
