package hu.kalmancheysandor.applications.dominions.apis.ai.engine.hugo.evalutor;

import hu.kalmancheysandor.applications.dominions.apis.ai.engine.hugo.evalution.HugoHeuristicEvaluation;
import hu.kalmancheysandor.applications.dominions.apis.ai.engine.hugo.interfaces.IHugoHeuristicEvaluation;
import hu.kalmancheysandor.applications.dominions.apis.ai.engine.hugo.interfaces.IHugoHeuristicEvaluator;
import hu.kalmancheysandor.applications.dominions.apis.game.common.representation.state.GameState;

public class HugoH0HeuristicEvaluator extends THugoHeuristicEvaluator implements IHugoHeuristicEvaluator {

    @Override
    public IHugoHeuristicEvaluation evaluate(GameState gameState, int supportedPlayerKey) {
        return new HugoHeuristicEvaluation(0);
    }
}
