package hu.kalmancheysandor.applications.dominions.apis.server.ai.otto.shared.dto.variant;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OttoVariantCreateResponse {
    private String uuid;
    private String name;
    private String evaluatorUuid;
    private String evaluatorName;
    private boolean enabled;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateCreated;
}
