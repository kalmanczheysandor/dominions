package hu.kalmancheysandor.applications.dominions.apis.ai.neural;


import hu.kalmancheysandor.applications.dominions.apis.game.common.representation.state.GameState;

public interface IHeuristicEvaluator{
    public IHeuristicEvaluation evaluate(GameState gameState, int supportedPlayerKey);
}
