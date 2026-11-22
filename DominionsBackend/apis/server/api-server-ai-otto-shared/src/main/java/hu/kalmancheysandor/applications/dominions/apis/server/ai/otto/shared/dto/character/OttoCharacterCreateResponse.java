package hu.kalmancheysandor.applications.dominions.apis.server.ai.otto.shared.dto.character;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class OttoCharacterCreateResponse {
    private String uuid;
    private String name;
    private String code;
    private String variantUuid;
    private String variantName;
    private boolean enabled;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateCreated;
}
