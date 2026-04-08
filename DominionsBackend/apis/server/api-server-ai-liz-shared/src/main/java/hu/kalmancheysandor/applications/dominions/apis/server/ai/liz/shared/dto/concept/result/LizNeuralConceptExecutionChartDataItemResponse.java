package hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.dto.concept.result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class LizNeuralConceptExecutionChartDataItemResponse {
    private int executionCount;
    private Integer executionKeyBest;
    private BigDecimal bestPrecision;
    private List<BigDecimal> precisions;
}
