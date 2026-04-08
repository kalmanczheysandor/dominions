package hu.kalmancheysandor.applications.dominions.apis.server.user.admin.dto.account.permission;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Data
@NoArgsConstructor
@Validated
public class AdminPermissionGroupCreateRequest {

//    @NotNull(message = "Field must not be null")
//    @Email(message="Field must be a in e-mail format")
//    @NotBlank(message = "Field must not be blank")
    private String name;
    private boolean enabled;

    private List<String> permissions;
}
