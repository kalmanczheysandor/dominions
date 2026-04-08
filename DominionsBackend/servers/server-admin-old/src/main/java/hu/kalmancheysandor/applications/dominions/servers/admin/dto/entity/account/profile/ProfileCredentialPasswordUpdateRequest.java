package hu.kalmancheysandor.applications.dominions.servers.admin.dto.entity.account.profile;

import hu.kalmancheysandor.applications.dominions.servers.admin.utils.validate.ValidPassword;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProfileCredentialPasswordUpdateRequest {

    @ValidPassword(minLength = 8,maxLength = 50, canBeEmpty = false)
    private String currentPassword;

    @ValidPassword(minLength = 8,maxLength = 50, canBeEmpty = false)
    private String newPassword;

    private String confirmNewPassword;

}
