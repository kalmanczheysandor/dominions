package hu.kalmancheysandor.applications.dominions.apis.ai.neural;


import hu.kalmancheysandor.applications.dominions.apis.ai.general.common.IAiEngine;

public interface INeuralAiEngine extends IAiEngine {
    public void registerEnemyNetwork(String userUuid,INeuralNetwork neuralNetwork);
}
