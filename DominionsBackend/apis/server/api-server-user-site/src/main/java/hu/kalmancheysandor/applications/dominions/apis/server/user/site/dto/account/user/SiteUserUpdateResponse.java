package hu.kalmancheysandor.applications.dominions.apis.server.user.site.dto.account.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SiteUserUpdateResponse {
    private String uuid;
    private String identifier;
    private String name;
    private boolean enabled;
    private boolean finalised;

    private List<String> permissionGroups;
}
