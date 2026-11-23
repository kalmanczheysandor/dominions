package hu.kalmancheysandor.applications.dominions.apis.ai.engine.hugo.evalutor;

import hu.kalmancheysandor.applications.dominions.apis.ai.engine.hugo.evalution.HugoHeuristicEvaluation;
import hu.kalmancheysandor.applications.dominions.apis.ai.engine.hugo.interfaces.IHugoHeuristicEvaluation;
import hu.kalmancheysandor.applications.dominions.apis.ai.engine.hugo.interfaces.IHugoHeuristicEvaluator;
import hu.kalmancheysandor.applications.dominions.apis.game.common.representation.state.GameState;

import static hu.kalmancheysandor.applications.dominions.apis.util.validate.asserted.VariableAssert.*;

public class HugoH1HeuristicEvaluator extends THugoHeuristicEvaluator implements IHugoHeuristicEvaluator {

    @Override
    public IHugoHeuristicEvaluation evaluate(GameState gameState, int supportedPlayerKey) {
        // Parameter checking
        assertObjectIsNotNull(gameState, "GameState");
        assertIntNotNegative(supportedPlayerKey, "supportedPlayerKey");
        assertIntIsInInterval(supportedPlayerKey, "supportedPlayerKey", 0, gameState.getOpponents().length - 1);

        // When supported player is out of game so, dead
        if (!isPlayerAlive(gameState, supportedPlayerKey)) {
            return new HugoHeuristicEvaluation(0.0);
        }

        // One remaining player and it is tha supported player
        if (countActivePlayers(gameState, supportedPlayerKey) == 1) {
            return new HugoHeuristicEvaluation(1.0);
        }

        // Two remaining player and fulfilled board
        if (countActivePlayers(gameState, supportedPlayerKey) == 2 && emptyCellCount(gameState) == 0) {
            if ((2 * ownedCellCount(gameState, supportedPlayerKey)) > allCellCount(gameState)) {// When: supported player wins
                return new HugoHeuristicEvaluation(1.0);
            } else if ((2 * ownedCellCount(gameState, supportedPlayerKey)) == allCellCount(gameState)) { // When: both player equal
                return new HugoHeuristicEvaluation(0.5);
            } else if ((2 * ownedCellCount(gameState, supportedPlayerKey)) < allCellCount(gameState)) {  // When: supported player loses
                return new HugoHeuristicEvaluation(0.0);
            }
        }


        // Other cases
        return new HugoHeuristicEvaluation((double) ownedCellCount(gameState, supportedPlayerKey) / (double)allCellCount(gameState));
    }
}






