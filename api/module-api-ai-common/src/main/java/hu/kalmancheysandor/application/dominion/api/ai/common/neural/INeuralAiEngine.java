package hu.kalmancheysandor.application.dominion.api.ai.common.neural;

import hu.kalmancheysandor.application.dominion.api.ai.common.IAiEngine;

import java.util.List;

public interface INeuralAiEngine extends IAiEngine {
    void train(List<NeuralTrainingData> trainingDataList);
}
