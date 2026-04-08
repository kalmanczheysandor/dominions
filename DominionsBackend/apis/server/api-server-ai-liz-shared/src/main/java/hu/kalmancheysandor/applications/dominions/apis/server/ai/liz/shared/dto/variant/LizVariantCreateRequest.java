package hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.dto.variant;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Validated
public class LizVariantCreateRequest {
    private String name;
    private int confSearchDepth;
    private String conceptUuid;
    private boolean enabled;
}
