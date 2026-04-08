package hu.kalmancheysandor.applications.dominions.apis.ai.neural;


import hu.kalmancheysandor.applications.dominions.apis.ai.common.IAiEngine;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public interface INeuralAiEngine extends IAiEngine {
    public void registerEnemyNetwork(String userUuid,INeuralNetwork neuralNetwork);
}
