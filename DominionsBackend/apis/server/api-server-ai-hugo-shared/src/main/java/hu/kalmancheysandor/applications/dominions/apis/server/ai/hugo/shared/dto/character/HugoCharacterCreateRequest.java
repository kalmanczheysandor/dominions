package hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.dto.character;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

@Data
@NoArgsConstructor
@Validated
public class HugoCharacterCreateRequest {
    private String name;
    private String code;
    private String variantUuid;
    private boolean enabled;
}
