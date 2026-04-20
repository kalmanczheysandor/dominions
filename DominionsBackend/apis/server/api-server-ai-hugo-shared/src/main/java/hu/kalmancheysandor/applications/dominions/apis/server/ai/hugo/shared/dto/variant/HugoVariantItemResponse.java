package hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.dto.variant;

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
public class HugoVariantItemResponse {
    private String uuid;
    private String name;
    private int confSearchDepth;
    private String heuristicUuid;
    private String heuristicName;
    private boolean enabled;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateCreated;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateModified;

}
