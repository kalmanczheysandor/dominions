package hu.kalmancheysandor.applications.dominions.apis.ai.engine.hugo;

import hu.kalmancheysandor.applications.dominions.apis.ai.neural.IHeuristicEvaluation;
import hu.kalmancheysandor.applications.dominions.apis.ai.neural.IHeuristicEvaluator;
import hu.kalmancheysandor.applications.dominions.apis.game.common.representation.state.GameState;

public class HugoSecondHeuristicEvaluator implements IHeuristicEvaluator {

    @Override
    public IHeuristicEvaluation evaluate(GameState gameState, int supportedPlayerKey) {
        int allCellCount = gameState.getCellCount();
        int emptyCellCount = gameState.getEmptyCellCount();
        double occupiedCellCount = allCellCount - emptyCellCount;
        int playerCount = gameState.getPlayerCount();
        double myOccupiedCellCount = gameState.getOccupiedCellsCountOfPlayer(supportedPlayerKey);

        double occupancyShare = myOccupiedCellCount / occupiedCellCount;
        return new HugoHeuristicEvaluation(occupancyShare);
    }
}
