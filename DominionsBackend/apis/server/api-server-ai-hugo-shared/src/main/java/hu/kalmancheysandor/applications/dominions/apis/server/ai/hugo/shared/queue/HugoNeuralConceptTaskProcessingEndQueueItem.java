package hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.queue;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HugoNeuralConceptTaskProcessingEndQueueItem {
    private int conceptId;
    private int executionId;


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        HugoNeuralConceptTaskProcessingEndQueueItem that = (HugoNeuralConceptTaskProcessingEndQueueItem) o;
        return conceptId == that.conceptId && executionId == that.executionId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(conceptId, executionId);
    }

    @Override
    public String toString() {
        return "LizNeuralConceptTaskProcessingEndQueueItem{" +
                "conceptId=" + conceptId +
                ", executionId=" + executionId +
                '}';
    }
}
