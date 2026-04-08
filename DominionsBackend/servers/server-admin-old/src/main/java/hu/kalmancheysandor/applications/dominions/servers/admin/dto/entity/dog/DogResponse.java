package hu.kalmancheysandor.applications.dominions.servers.admin.dto.entity.dog;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DogResponse {
    private String uuid;
    private String prn;
    private String name;
    private String breedUuid;
    private String breedName;
    private String siteUuid;
    private String siteName;

    private String note;
    private boolean enabled;
}
