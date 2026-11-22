package hu.kalmancheysandor.applications.dominions.apis.ai.neural;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NeuralNetworkTrainingSnapshot {
    private int turn;
    private int currentBestTurn;
    private NeuralNetworkEvaluationResult evaluationResult;
    private int iterationCount;

}
