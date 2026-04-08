package hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.dto.variant;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class LizVariantAccessResponse {
    private String uuid;
    private String name;
    private int confSearchDepth;
    private String conceptUuid;
    private String conceptName;
    private boolean enabled;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateCreated;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateModified;
}
