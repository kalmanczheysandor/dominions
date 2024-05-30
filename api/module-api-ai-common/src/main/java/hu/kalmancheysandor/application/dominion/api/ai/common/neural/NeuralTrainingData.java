package hu.kalmancheysandor.application.dominion.api.ai.common.neural;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NeuralTrainingData {
    private int reserveSize;
    private int enemiesCount;
    private int[] cellOwners;
    private int[] cellDefendersSize;

    private int chosenTarget;
    private int chosenTroopSize;

}
