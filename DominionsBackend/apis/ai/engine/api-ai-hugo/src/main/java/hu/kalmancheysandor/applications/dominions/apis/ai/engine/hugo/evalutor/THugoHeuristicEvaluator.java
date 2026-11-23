package hu.kalmancheysandor.applications.dominions.apis.ai.engine.hugo.evalutor;

import hu.kalmancheysandor.applications.dominions.apis.game.common.representation.state.GameState;
import hu.kalmancheysandor.applications.dominions.apis.game.common.representation.state.GameStatePlayer;

import static hu.kalmancheysandor.applications.dominions.apis.util.validate.asserted.VariableAssert.assertIntNotNegative;
import static hu.kalmancheysandor.applications.dominions.apis.util.validate.asserted.VariableAssert.assertObjectIsNotNull;

public class THugoHeuristicEvaluator {


    protected static boolean isPlayerAlive(GameState gameState, int observedPlayerKey) {
        // Parameter checking
        assertObjectIsNotNull(gameState, "GameState");
        assertIntNotNegative(observedPlayerKey, "ObservedPlayerKey");

        GameStatePlayer playerObj = gameState.getOpponent(observedPlayerKey);

        return playerObj.isAlive();
    }

    protected static int countActiveEnemies(GameState gameState, int observedPlayerKey) {
        // Parameter checking
        assertObjectIsNotNull(gameState, "GameState");
        assertIntNotNegative(observedPlayerKey, "ObservedPlayerKey");

        return gameState.getAlivePlayerCount() - 1;
    }

    protected static int countActivePlayers(GameState gameState, int observedPlayerKey) {
        // Parameter checking
        assertObjectIsNotNull(gameState, "GameState");
        assertIntNotNegative(observedPlayerKey, "ObservedPlayerKey");

        return gameState.getAlivePlayerCount();
    }

    protected static int emptyCellCount(GameState gameState) {
        // Parameter checking
        assertObjectIsNotNull(gameState, "GameState");

        return gameState.getEmptyCellCount();
    }

    protected static int ownedCellCount(GameState gameState,int observedPlayerKey) {
        // Parameter checking
        assertObjectIsNotNull(gameState, "GameState");
        assertIntNotNegative(observedPlayerKey, "ObservedPlayerKey");

        return gameState.getOccupiedCellsCountOfPlayer(observedPlayerKey);
    }
  protected static int allCellCount(GameState gameState) {
        // Parameter checking
        assertObjectIsNotNull(gameState, "GameState");

        return gameState.getCellCount();
    }


}
