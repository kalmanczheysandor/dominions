package hu.kalmancheysandor.applications.dominions.servers.admin.dto.entity.account.profile;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProfileCredentialPasswordAccessResponse {
    private String uuid;
    private String identifier;
}
