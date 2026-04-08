package hu.kalmancheysandor.applications.dominions.servers.admin.dto.entity.account.user;

import hu.kalmancheysandor.applications.dominions.servers.admin.utils.validate.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Data
@NoArgsConstructor
@Validated
public class UserCreateRequest {

    @NotNull(message = "Field must not be null")
    @Email(message="Field must be a in e-mail format")
    @NotBlank(message = "Field must not be blank")
    private String identifier;

    @ValidPassword(minLength = 8,maxLength = 50, canBeEmpty = false)
    private String password;

    private String confirmPassword;

    @NotNull(message = "Field must not be null")
    @NotBlank(message = "Field must not be blank")
    private String name;

    private boolean enabled;

    private List<String> permissionGroups;

}
