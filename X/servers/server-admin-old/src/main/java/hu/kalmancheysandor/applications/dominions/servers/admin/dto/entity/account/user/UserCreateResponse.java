package hu.kalmancheysandor.applications.dominions.servers.admin.dto.entity.account.user;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class UserCreateResponse {
    private String uuid;
    private String identifier;
    private String name;
    private boolean enabled;
    private List<String> permissionGroups;
}
