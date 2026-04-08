package hu.kalmancheysandor.applications.dominions.apis.ai.liz;

import hu.kalmancheysandor.applications.dominions.apis.ai.neural.INeuralNetworkTrainingConfiguration;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LizNeuralNetworkTrainingConfiguration implements INeuralNetworkTrainingConfiguration {
    private int maxIterationsPerTurn;
    private int maxTurn;
    private double learningRate = 0.0;

    public static LizNeuralNetworkTrainingConfiguration generateInstance(INeuralNetworkTrainingConfiguration trainingConfiguration) {
        if (!(trainingConfiguration instanceof LizNeuralNetworkTrainingConfiguration)) {
            throw new RuntimeException("Neural training-config is not of type LizNeuralNetworkTrainingConfiguration");
        }
        return (LizNeuralNetworkTrainingConfiguration) trainingConfiguration;
    }
}
