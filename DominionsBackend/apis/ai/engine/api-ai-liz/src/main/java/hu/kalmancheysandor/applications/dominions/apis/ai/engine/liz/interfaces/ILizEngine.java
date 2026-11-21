package hu.kalmancheysandor.applications.dominions.apis.ai.engine.liz.interfaces;

import hu.kalmancheysandor.applications.dominions.apis.ai.general.common.IAiAlgorithm;
import hu.kalmancheysandor.applications.dominions.apis.ai.neural.network.INeuralNetwork;

public interface ILizEngine extends IAiAlgorithm {
    public void registerEnemyNetwork(String userUuid, INeuralNetwork neuralNetwork);
    public void registerOperatorScoreEvaluator(ILizOperatorScoreEvaluator evaluator);
}
