package hu.kalmancheysandor.applications.dominions.servers.admin.dto.entity.breed;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BreedResponse {
    private String uuid;
    private String name;
    private boolean enabled;
}
