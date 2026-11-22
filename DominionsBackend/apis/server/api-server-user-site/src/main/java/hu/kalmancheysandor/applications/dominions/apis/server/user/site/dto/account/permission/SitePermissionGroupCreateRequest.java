package hu.kalmancheysandor.applications.dominions.apis.server.user.site.dto.account.permission;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Data
@NoArgsConstructor
@Validated
public class SitePermissionGroupCreateRequest {
    private String name;
    private boolean enabled;
    private List<String> permissions;
}
