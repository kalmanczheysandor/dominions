package hu.kalmancheysandor.applications.dominions.apis.ai.engine.otto.interfaces;

import hu.kalmancheysandor.applications.dominions.apis.game.common.representation.state.GameState;

public interface IOttoOperatorScoreEvaluator {
    public IOttoOperatorScoreEvaluation evaluate(GameState gameState, int operatorKey, int observedPlayerKey);
}
