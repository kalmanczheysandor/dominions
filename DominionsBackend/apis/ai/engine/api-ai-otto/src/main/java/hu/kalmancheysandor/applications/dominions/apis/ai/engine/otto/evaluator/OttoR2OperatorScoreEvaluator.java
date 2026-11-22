package hu.kalmancheysandor.applications.dominions.apis.ai.engine.otto.evaluator;


import hu.kalmancheysandor.applications.dominions.apis.ai.engine.otto.interfaces.IOttoOperatorScoreEvaluation;
import hu.kalmancheysandor.applications.dominions.apis.ai.engine.otto.interfaces.IOttoOperatorScoreEvaluator;
import hu.kalmancheysandor.applications.dominions.apis.ai.engine.otto.evaluation.OttoOperatorScoreEvaluation;
import hu.kalmancheysandor.applications.dominions.apis.game.common.representation.state.GameState;

public class OttoR2OperatorScoreEvaluator implements IOttoOperatorScoreEvaluator {
    @Override
    public IOttoOperatorScoreEvaluation evaluate(GameState gameState, int operatorKey, int observedPlayerKey) {
        return new OttoOperatorScoreEvaluation(0.0);
    }
}
