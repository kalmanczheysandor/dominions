package hu.kalmancheysandor.applications.dominions.apis.ai.engine.liz;

import hu.kalmancheysandor.applications.dominions.apis.ai.neural.network.INeuralNetworkModelInput;
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
public class LizNeuralNetworkModelInput implements INeuralNetworkModelInput {
    private int[] cellOccupierIndex;
    private int[] cellGarrisonSize;
    private int[] playerTerritorySize;
    private int[] playerReserveSize;
    private boolean[] playerIsAlive;




    public static LizNeuralNetworkModelInput convertInstance(INeuralNetworkModelInput modelInputObj) {
        if (!(modelInputObj instanceof LizNeuralNetworkModelInput)) {
            throw new RuntimeException("Neural modelOutputObj is not of type LizNeuralNetworkModelOutput");
        }
        return (LizNeuralNetworkModelInput) modelInputObj;
    }

    public static List<LizNeuralNetworkModelInput> convertModelInputList(List<INeuralNetworkModelInput> modelInputList) {
        // Casting training data
        List<LizNeuralNetworkModelInput> inputList = new ArrayList<>();
        for (INeuralNetworkModelInput modelInputObj : modelInputList) {
            inputList.add(convertInstance(modelInputObj));
        }
        return inputList;
    }
}
