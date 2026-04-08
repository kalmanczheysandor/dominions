package hu.kalmancheysandor.applications.dominions.apis.ai.neural;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NeuralNetworkEvaluationResult {
    private double precisionRate = 0.0;
}
