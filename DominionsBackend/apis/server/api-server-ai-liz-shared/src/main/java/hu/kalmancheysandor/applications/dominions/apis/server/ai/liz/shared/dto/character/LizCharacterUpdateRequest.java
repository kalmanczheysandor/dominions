package hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.dto.character;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

@Data
@NoArgsConstructor
@Validated
public class LizCharacterUpdateRequest {
    private String name;
    private String code;
    private String variantUuid;
    private boolean enabled;
}
