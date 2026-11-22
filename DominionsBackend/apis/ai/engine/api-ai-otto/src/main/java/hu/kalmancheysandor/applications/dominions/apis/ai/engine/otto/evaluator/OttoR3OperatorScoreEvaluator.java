package hu.kalmancheysandor.applications.dominions.apis.ai.engine.otto.evaluator;


import hu.kalmancheysandor.applications.dominions.apis.ai.engine.otto.interfaces.IOttoOperatorScoreEvaluation;
import hu.kalmancheysandor.applications.dominions.apis.ai.engine.otto.interfaces.IOttoOperatorScoreEvaluator;
import hu.kalmancheysandor.applications.dominions.apis.ai.engine.otto.evaluation.OttoOperatorScoreEvaluation;
import hu.kalmancheysandor.applications.dominions.apis.game.common.representation.state.GameState;
import hu.kalmancheysandor.applications.dominions.apis.game.common.representation.state.GameStateCell;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class OttoR3OperatorScoreEvaluator implements IOttoOperatorScoreEvaluator {

    @Override
    public IOttoOperatorScoreEvaluation evaluate(GameState gameState, int operatorKey, int observedPlayerKey) {
        return new OttoOperatorScoreEvaluation(
                collectEnemyPlayerCountInTheNeighbour(gameState, operatorKey)
        );
    }


    private int collectEnemyPlayerCountInTheNeighbour(GameState gameState, int cellKey) {
        Map<Integer, GameStateCell> neighbourCells = collectNeighbourCellsOfACell(gameState, cellKey);
        Set<Integer> enemyKeys = new HashSet<>();
        for (Map.Entry<Integer, GameStateCell> entry : neighbourCells.entrySet()) {
            Integer occupierKey = entry.getValue().getOccupierKey();
            enemyKeys.add(occupierKey);
        }
        return enemyKeys.size();
    }

    protected static Map<Integer, GameStateCell> collectNeighbourCellsOfACell(GameState state, int cellKey) {
        Map<Integer, GameStateCell> realNeighbours = new HashMap<>();

        boolean[] neighboursRow = state.getNeighboursMatrix()[cellKey];
        for (int neighbourKey = 0; neighbourKey < neighboursRow.length; neighbourKey++) {
            if (neighboursRow[neighbourKey] == true) {
                realNeighbours.put(neighbourKey, state.getCell(neighbourKey));
            }
        }
        return realNeighbours;
    }
}
