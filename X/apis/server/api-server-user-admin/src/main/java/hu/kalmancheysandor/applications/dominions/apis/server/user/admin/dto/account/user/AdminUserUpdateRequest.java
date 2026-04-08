package hu.kalmancheysandor.applications.dominions.apis.server.user.admin.dto.account.user;

import hu.kalmancheysandor.applications.dominions.apis.util.validate.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class AdminUserUpdateRequest {

    @NotNull(message = "Field must not be null")
    @NotBlank(message = "Field must not be blank")
    @Email(message="Field must be a in e-mail format")
    private String identifier;

    @ValidPassword(minLength = 8,maxLength = 50, canBeEmpty = true)
    private String password;

    private String confirmPassword;

    @NotNull(message = "Field must not be null")
    @NotBlank(message = "Field must not be blank")
    private String name;

    private boolean enabled;
    private boolean finalised;

    private List<String> permissionGroups;

    private String imageBase64;
}
