package hu.kalmancheysandor.applications.dominions.apis.ai.engine.otto.interfaces;

import hu.kalmancheysandor.applications.dominions.apis.ai.general.common.IAiAlgorithm;

public interface IOttoEngine extends IAiAlgorithm {
    public  void registerOperatorScoreEvaluator(IOttoOperatorScoreEvaluator evaluator);
}
