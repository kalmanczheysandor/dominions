package hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.queue;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LizNeuralConceptTaskProcessingQueueItem {
    private int conceptId;
    private int executionId;
    private int scenarioId;
    private int playerId;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        LizNeuralConceptTaskProcessingQueueItem that = (LizNeuralConceptTaskProcessingQueueItem) o;
        return conceptId == that.conceptId && executionId == that.executionId && scenarioId == that.scenarioId && playerId == that.playerId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(conceptId, executionId, scenarioId, playerId);
    }

    @Override
    public String toString() {
        return "LizNeuralConceptTaskProcessingQueueItem{" +
                "conceptId=" + conceptId +
                ", executionId=" + executionId +
                ", scenarioId=" + scenarioId +
                ", playerId=" + playerId +
                '}';
    }
}
