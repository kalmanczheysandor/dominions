package hu.kalmancheysandor.applications.dominions.servers.admin.dto.account.admin.settings;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AdminAccountSettingsProfileDetailsUpdateRequest {

    @NotNull(message = "Field must not be null")
    @NotBlank(message = "Field must not be blank")
    private String name;

    private String imageBase64;
}
