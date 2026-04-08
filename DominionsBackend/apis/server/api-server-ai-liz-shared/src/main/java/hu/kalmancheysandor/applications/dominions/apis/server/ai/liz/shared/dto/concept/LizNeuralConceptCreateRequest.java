package hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.dto.concept;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@Validated
public class LizNeuralConceptCreateRequest {
    private String name;
    private int confMaxIterationsPerTurn;
    private int confMaxTurn;
    private BigDecimal confLearningRate;

    private boolean enabled;
}
