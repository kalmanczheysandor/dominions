package hu.kalmancheysandor.applications.dominions.apis.ai.engine.liz;

import hu.kalmancheysandor.applications.dominions.apis.ai.neural.network.INeuralNetworkModelOutput;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LizNeuralNetworkModelOutput implements INeuralNetworkModelOutput {

    private double[] operatorProbability;

    public static LizNeuralNetworkModelOutput createDefault() {
        return new LizNeuralNetworkModelOutput();
    }

    public static LizNeuralNetworkModelOutput convertInstance(INeuralNetworkModelOutput modelOutputObj) {
        if (!(modelOutputObj instanceof LizNeuralNetworkModelOutput)) {
            throw new RuntimeException("Neural modelOutputObj is not of type LizNeuralNetworkModelOutput");
        }
        return (LizNeuralNetworkModelOutput) modelOutputObj;
    }

    public static List<LizNeuralNetworkModelOutput> convertModelOutputList(List<INeuralNetworkModelOutput> modelOutputList) {
        // Casting
        List<LizNeuralNetworkModelOutput> outputList = new ArrayList<>();
        for (INeuralNetworkModelOutput modelOutputObj : modelOutputList) {
            outputList.add(convertInstance(modelOutputObj));
        }
        return outputList;
    }
}
