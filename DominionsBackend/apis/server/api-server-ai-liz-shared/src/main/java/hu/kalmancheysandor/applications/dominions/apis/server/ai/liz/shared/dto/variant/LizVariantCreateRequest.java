package hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.dto.variant;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;


@Data
@NoArgsConstructor
@Validated
public class LizVariantCreateRequest {
    private String name;
    private int confSearchDepth;
    private String conceptUuid;
    private boolean enabled;
}
