package hu.kalmancheysandor.applications.dominions.apis.ai.engine.liz.interfaces;

import hu.kalmancheysandor.applications.dominions.apis.ai.general.common.AiDecisionContext;
import hu.kalmancheysandor.applications.dominions.apis.ai.neural.network.INeuralNetwork;
import hu.kalmancheysandor.applications.dominions.apis.game.common.representation.state.GameState;

import java.util.Map;

public interface ILizOperatorScoreEvaluator {
    public ILizOperatorScoreEvaluation evaluate(ILizEvaluationInput input);
}
