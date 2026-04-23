package hu.kalmancheysandor.applications.dominions.apis.ai.neural;


import hu.kalmancheysandor.applications.dominions.apis.ai.common.IAiEngine;
import jakarta.validation.constraints.NotEmpty;

public interface IHeuristicAiEngine extends IAiEngine {
    public void registerHeuristicEvaluator(IHeuristicEvaluator evaluator);
}
