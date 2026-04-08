package hu.kalmancheysandor.applications.dominions.apis.server.user.site.dto.account.user;


import hu.kalmancheysandor.applications.dominions.apis.general.utils.validation.password.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SiteUserSaveRequest {

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
    private boolean finalised;

    private List<String> permissionGroups;

    private String imageBase64;

}
