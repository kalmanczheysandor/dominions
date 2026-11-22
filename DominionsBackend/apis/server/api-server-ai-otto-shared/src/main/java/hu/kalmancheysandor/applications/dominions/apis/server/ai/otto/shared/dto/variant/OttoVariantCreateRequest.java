package hu.kalmancheysandor.applications.dominions.apis.server.ai.otto.shared.dto.variant;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

@Data
@NoArgsConstructor
@Validated
public class OttoVariantCreateRequest {
    private String name;
    private String evaluatorUuid;
    private boolean enabled;
}
