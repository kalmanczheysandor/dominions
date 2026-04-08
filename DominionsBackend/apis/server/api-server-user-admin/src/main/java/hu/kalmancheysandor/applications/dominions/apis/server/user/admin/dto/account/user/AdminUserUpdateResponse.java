package hu.kalmancheysandor.applications.dominions.apis.server.user.admin.dto.account.user;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class AdminUserUpdateResponse {
    private String uuid;
    private String identifier;
    private String name;
    private boolean enabled;
    private boolean finalised;

    private List<String> permissionGroups;
}
