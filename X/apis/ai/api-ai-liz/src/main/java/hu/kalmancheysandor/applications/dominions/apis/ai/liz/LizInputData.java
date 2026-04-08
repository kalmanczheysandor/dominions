package hu.kalmancheysandor.applications.dominions.apis.ai.liz;

import hu.kalmancheysandor.applications.dominions.apis.ai.neural.INeuralInputData;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class LizInputData implements INeuralInputData {
    private int attackPower;
    private int[] cellOwnerRanks;
    private int[] cellDefendersSize;
    private int chosenTarget;


    public static LizInputData generateInstance(INeuralInputData trainingData) {
        if (!(trainingData instanceof LizInputData)) {
            throw new RuntimeException("Neural training data is not of type LizTrainingData");
        }
        return (LizInputData) trainingData;
    }

    public static List<LizInputData> convertInputDataList(List<INeuralInputData> trainingDataList) {
        // Casting training data
        List<LizInputData> trainingList = new ArrayList<>();
        for (INeuralInputData data : trainingDataList) {
            trainingList.add(generateInstance(data));
        }
        return trainingList;
    }
}
