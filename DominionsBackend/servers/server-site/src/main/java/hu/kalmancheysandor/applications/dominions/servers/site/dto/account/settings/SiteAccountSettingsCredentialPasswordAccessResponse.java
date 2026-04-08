package hu.kalmancheysandor.applications.dominions.servers.site.dto.account.settings;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SiteAccountSettingsCredentialPasswordAccessResponse {
    private String uuid;
    private String identifier;
}
