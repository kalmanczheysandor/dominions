package hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.dto.variant;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

@Data
@NoArgsConstructor
@Validated
public class HugoVariantCreateRequest {
    private String name;
    private int confSearchDepth;
    private String heuristicUuid;
    private boolean enabled;
}
