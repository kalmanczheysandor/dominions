package hu.kalmancheysandor.applications.dominions.apis.ai.engine.hugo.interfaces;
import hu.kalmancheysandor.applications.dominions.apis.game.common.representation.state.GameState;

public interface IHugoHeuristicEvaluator {
    public IHugoHeuristicEvaluation evaluate(GameState gameState, int supportedPlayerKey);
}
