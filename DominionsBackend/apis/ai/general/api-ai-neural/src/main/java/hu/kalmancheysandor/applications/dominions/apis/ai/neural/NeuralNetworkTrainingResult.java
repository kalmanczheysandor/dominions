package hu.kalmancheysandor.applications.dominions.apis.ai.neural;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NeuralNetworkTrainingResult {
    private double precisionRate = 0.0;
    private int turnBest;
    private int turnCount;
    private Status status;

    public enum Status {
        DONE,
        CANCELED
    }

}
