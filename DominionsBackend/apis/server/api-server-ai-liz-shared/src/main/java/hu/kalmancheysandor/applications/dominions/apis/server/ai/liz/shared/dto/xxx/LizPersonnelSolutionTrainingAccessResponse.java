package hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.dto.xxx;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LizPersonnelSolutionTrainingAccessResponse {
    private String uuid;
    private String solutionUuid;
    private String title;
    private String processPhase;
    private String processState;
    private boolean applicable;
    private boolean enabled;
}
