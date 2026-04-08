package hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.dto.solution;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@Validated
public class LizPersonnelSolutionUpdateRequest {
    private String title;
    private int confTrainingTurnMax;
    private int confTrainingTurnIteration;
    private BigDecimal confTrainingLearningRate;
    private boolean enabled;
}
