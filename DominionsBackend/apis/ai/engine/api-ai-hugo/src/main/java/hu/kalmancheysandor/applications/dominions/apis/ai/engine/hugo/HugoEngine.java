package hu.kalmancheysandor.applications.dominions.apis.ai.engine.hugo;

import hu.kalmancheysandor.applications.dominions.apis.ai.algorithm.simultan.TSimultaNAlgorithm;
import hu.kalmancheysandor.applications.dominions.apis.ai.engine.hugo.interfaces.IHugoEngine;
import hu.kalmancheysandor.applications.dominions.apis.ai.engine.hugo.interfaces.IHugoHeuristicEvaluator;
import hu.kalmancheysandor.applications.dominions.apis.game.common.representation.state.GameState;

import java.util.List;

public class HugoEngine extends TSimultaNAlgorithm implements IHugoEngine {
    private IHugoHeuristicEvaluator heuristicEvaluator;

    @Override
    protected double evaluateHeuristicValueOfGameState(GameState gameState, int supportedPlayerKey) {
        return this.heuristicEvaluator.evaluate(gameState, supportedPlayerKey).getHeuristicValue();
    }


    protected double modulateHeuristicValues(List<Double> gameStateVariantValues) {
        return gameStateVariantValues.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElseThrow();

    }

    @Override
    public void registerHeuristicEvaluator(IHugoHeuristicEvaluator evaluator) {
        this.heuristicEvaluator = evaluator;
    }
}
