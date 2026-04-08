package hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.dto.solution;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LizPersonnelSolutionItemResponse {
    private String uuid;
    private String personnelUuid;
    private String title;
    private int confTrainingTurnMax;
    private int confTrainingTurnIteration;
    private BigDecimal confTrainingLearningRate;
    private boolean enabled;

}
