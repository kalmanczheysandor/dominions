package hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.dto.xxx;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

@Data
@NoArgsConstructor
@Validated
public class LizPersonnelSolutionTrainingUpdateRequest {
    private String title;
    private boolean enabled;
}
