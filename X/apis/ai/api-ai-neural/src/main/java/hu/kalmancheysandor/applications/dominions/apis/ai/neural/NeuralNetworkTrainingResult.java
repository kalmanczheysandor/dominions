package hu.kalmancheysandor.applications.dominions.apis.ai.neural;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NeuralNetworkTrainingResult {
    private double precisionRate = 0.0;
    private List<NeuralNetworkTrainingResult.Snapshot> snapshotsList = new ArrayList<>();

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Snapshot {
        private int turn;
        private NeuralNetworkEvaluationResult evaluationResult;
        private int iterationCount;
    }

}
