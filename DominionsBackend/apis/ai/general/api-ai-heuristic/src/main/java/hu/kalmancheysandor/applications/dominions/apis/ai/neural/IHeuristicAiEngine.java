package hu.kalmancheysandor.applications.dominions.apis.ai.neural;


import hu.kalmancheysandor.applications.dominions.apis.ai.general.common.IAiEngine;

public interface IHeuristicAiEngine extends IAiEngine {
    public void registerHeuristicEvaluator(IHeuristicEvaluator evaluator);
}
