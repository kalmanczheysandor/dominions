package hu.kalmancheysandor.applications.dominions.apis.ai.engine.hugo.interfaces;

import hu.kalmancheysandor.applications.dominions.apis.ai.general.common.IAiAlgorithm;

public interface IHugoEngine extends IAiAlgorithm {
    public void registerHeuristicEvaluator(IHugoHeuristicEvaluator evaluator);
    public void setSearchDepthLimit(int depthLimit);
}
