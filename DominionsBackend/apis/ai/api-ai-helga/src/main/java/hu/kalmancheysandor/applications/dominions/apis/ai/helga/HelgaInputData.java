package hu.kalmancheysandor.applications.dominions.apis.ai.helga;

import hu.kalmancheysandor.applications.dominions.apis.ai.neural.INeuralInputData;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class HelgaInputData implements INeuralInputData {
    private int attackPower;
    private int enemiesCount;
    private int[] cellOwners;
    private int[] cellDefendersSize;
    private int chosenTarget;
    private int chosenTroopSize;


    public static HelgaInputData convertTrainingData(INeuralInputData trainingData) {
        if (!(trainingData instanceof HelgaInputData)) {
            throw new RuntimeException("Neural training data is not of type HelgaTrainingData");
        }
        return (HelgaInputData) trainingData;
    }

    public static List<HelgaInputData> convertTrainingDataList(List<INeuralInputData> trainingDataList) {
        // Casting training data
        List<HelgaInputData> trainingList = new ArrayList<>();
        for (INeuralInputData data : trainingDataList) {
            trainingList.add(convertTrainingData(data));
        }
        return trainingList;
    }
}
