package hu.kalmancheysandor.applications.dominions.servers.site.dto.account.settings;

import hu.kalmancheysandor.applications.dominions.apis.util.validate.ValidPassword;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SiteAccountSettingsCredentialPasswordUpdateRequest {

    @ValidPassword(minLength = 8,maxLength = 50, canBeEmpty = false)
    private String currentPassword;

    @ValidPassword(minLength = 8,maxLength = 50, canBeEmpty = false)
    private String newPassword;

    private String confirmNewPassword;
}
