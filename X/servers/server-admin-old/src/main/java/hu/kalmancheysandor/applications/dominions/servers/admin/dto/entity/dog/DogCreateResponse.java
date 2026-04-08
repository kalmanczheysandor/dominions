package hu.kalmancheysandor.applications.dominions.servers.admin.dto.entity.dog;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DogCreateResponse {
    private String uuid;
    private String prn;
    private String name;
    private String breedUuid;
    private String siteUuid;
    private String note;
    private boolean enabled;
}
