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
public class NeuralNetworkTrainingSnapshot {
    private int turn;
    private int currentBestTurn;
    private NeuralNetworkEvaluationResult evaluationResult;
    private int iterationCount;

}
