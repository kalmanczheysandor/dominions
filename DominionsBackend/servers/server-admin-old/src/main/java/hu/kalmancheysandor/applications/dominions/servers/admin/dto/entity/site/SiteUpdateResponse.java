package hu.kalmancheysandor.applications.dominions.servers.admin.dto.entity.site;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SiteUpdateResponse {
    private String uuid;
    private String name;
    private String address;
    private String note;
    private boolean enabled;
}
