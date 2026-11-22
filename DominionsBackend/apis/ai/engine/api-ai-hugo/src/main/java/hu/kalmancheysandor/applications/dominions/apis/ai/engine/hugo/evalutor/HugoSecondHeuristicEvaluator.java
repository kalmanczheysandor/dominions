package hu.kalmancheysandor.applications.dominions.apis.ai.engine.hugo.evalutor;

import hu.kalmancheysandor.applications.dominions.apis.ai.engine.hugo.interfaces.IHugoHeuristicEvaluation;
import hu.kalmancheysandor.applications.dominions.apis.ai.engine.hugo.interfaces.IHugoHeuristicEvaluator;
import hu.kalmancheysandor.applications.dominions.apis.ai.engine.hugo.evalution.HugoHeuristicEvaluation;
import hu.kalmancheysandor.applications.dominions.apis.game.common.representation.state.GameState;

public class HugoSecondHeuristicEvaluator implements IHugoHeuristicEvaluator {

    @Override
    public IHugoHeuristicEvaluation evaluate(GameState gameState, int supportedPlayerKey) {
        int allCellCount = gameState.getCellCount();
        int emptyCellCount = gameState.getEmptyCellCount();
        double occupiedCellCount = allCellCount - emptyCellCount;
        int playerCount = gameState.getPlayerCount();
        double myOccupiedCellCount = gameState.getOccupiedCellsCountOfPlayer(supportedPlayerKey);

        double occupancyShare = myOccupiedCellCount / occupiedCellCount;
        return new HugoHeuristicEvaluation(occupancyShare);
    }
}
