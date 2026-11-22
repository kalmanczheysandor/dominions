package hu.kalmancheysandor.applications.dominions.apis.ai.neural;


import hu.kalmancheysandor.applications.dominions.apis.ai.general.common.IAiAlgorithm;
import hu.kalmancheysandor.applications.dominions.apis.ai.neural.network.INeuralNetwork;

public interface INeuralAiAlgorithm extends IAiAlgorithm {
    public void registerEnemyNetwork(String userUuid, INeuralNetwork neuralNetwork);
}
