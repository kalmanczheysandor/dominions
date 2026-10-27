package hu.kalmancheysandor.applications.dominions.apis.ai.engine.liz;

import hu.kalmancheysandor.applications.dominions.apis.ai.neural.network.INeuralNetworkModelInput;
import hu.kalmancheysandor.applications.dominions.apis.ai.neural.network.INeuralNetworkModelOutput;
import hu.kalmancheysandor.applications.dominions.apis.ai.neural.network.INeuralNetworkTrainingItem;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class LizNeuralNetworkTrainingItem implements INeuralNetworkTrainingItem {
    private LizNeuralNetworkModelInput input;
    private LizNeuralNetworkModelOutput output;

    public LizNeuralNetworkTrainingItem(LizNeuralNetworkModelInput input, LizNeuralNetworkModelOutput output) {
        this.input = input;
        this.output = output;
    }

    @Override
    public INeuralNetworkModelInput getNeuralNetworkModelInput() {
        return this.input;
    }

    @Override
    public INeuralNetworkModelOutput getNeuralNetworkModelOutput() {
        return this.output;
    }


    public static LizNeuralNetworkTrainingItem generateInstance(INeuralNetworkTrainingItem trainingItem) {
        if (!(trainingItem instanceof LizNeuralNetworkTrainingItem)) {
            throw new RuntimeException("Neural training item is not of type LizNeuralNetworkTrainingItem");
        }
        return (LizNeuralNetworkTrainingItem) trainingItem;
    }

    public static List<LizNeuralNetworkTrainingItem> convertTrainingItemList(List<INeuralNetworkTrainingItem> trainingItemList) {
        // Casting training item
        List<LizNeuralNetworkTrainingItem> itemList = new ArrayList<>();
        for (INeuralNetworkTrainingItem item : trainingItemList) {
            itemList.add(generateInstance(item));
        }
        return itemList;
    }
}
