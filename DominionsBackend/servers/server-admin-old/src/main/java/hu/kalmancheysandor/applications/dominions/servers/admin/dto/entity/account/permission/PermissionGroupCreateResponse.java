package hu.kalmancheysandor.applications.dominions.servers.admin.dto.entity.account.permission;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class PermissionGroupCreateResponse {
    private String uuid;
    private String name;
    private boolean enabled;

    private List<String> permissions;
}
