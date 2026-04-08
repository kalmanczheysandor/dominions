package hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.dto.concept.execution;

import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.entity.training.neural.LizNeuralExecution;
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
public class LizNeuralConceptExecutionStatusResponse {

    private int taskAllCount;

    private int taskPhaseCreatedCount;
    private int taskPhaseQueuedCount;
    private int taskPhasePausedCount;
    private int taskPhaseFinishedCount;

    private int taskResultPendingCount;
    private int taskResultFailedCount;
    private int taskResultSucceededCount;
    private int taskResultCanceledCount;

    private LizNeuralExecution.ProcessPhase processPhase;
}
