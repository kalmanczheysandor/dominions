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
public class LizNeuralConceptTaskProcessingBeginQueueItem {
    private int conceptId;
    private int executionId;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        LizNeuralConceptTaskProcessingBeginQueueItem that = (LizNeuralConceptTaskProcessingBeginQueueItem) o;
        return conceptId == that.conceptId && executionId == that.executionId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(conceptId, executionId);
    }

    @Override
    public String toString() {
        return "LizNeuralConceptTaskProcessingBeginQueueItem{" +
                "conceptId=" + conceptId +
                ", executionId=" + executionId +
                '}';
    }
}
