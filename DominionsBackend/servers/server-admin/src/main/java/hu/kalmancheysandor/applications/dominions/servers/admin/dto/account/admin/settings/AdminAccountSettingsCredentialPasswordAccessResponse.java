package hu.kalmancheysandor.applications.dominions.servers.admin.dto.account.admin.settings;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AdminAccountSettingsCredentialPasswordAccessResponse {
    private String uuid;
    private String identifier;
}
