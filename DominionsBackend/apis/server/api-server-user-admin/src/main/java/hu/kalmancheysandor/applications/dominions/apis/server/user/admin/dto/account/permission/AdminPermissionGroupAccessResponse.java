package hu.kalmancheysandor.applications.dominions.apis.server.user.admin.dto.account.permission;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class AdminPermissionGroupAccessResponse {
    private String uuid;
    private String name;
    private boolean enabled;

    private List<String> permissions;
}
