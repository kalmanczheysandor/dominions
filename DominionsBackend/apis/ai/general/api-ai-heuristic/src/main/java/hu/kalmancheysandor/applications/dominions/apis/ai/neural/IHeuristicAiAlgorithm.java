package hu.kalmancheysandor.applications.dominions.apis.ai.neural;


import hu.kalmancheysandor.applications.dominions.apis.ai.general.common.IAiAlgorithm;

public interface IHeuristicAiAlgorithm extends IAiAlgorithm {
    public void registerHeuristicEvaluator(IHeuristicEvaluator evaluator);
}
