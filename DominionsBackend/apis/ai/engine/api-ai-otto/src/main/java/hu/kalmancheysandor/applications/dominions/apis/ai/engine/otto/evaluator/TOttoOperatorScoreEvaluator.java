package hu.kalmancheysandor.applications.dominions.apis.ai.engine.otto.evaluator;

import hu.kalmancheysandor.applications.dominions.apis.game.common.representation.state.GameState;
import hu.kalmancheysandor.applications.dominions.apis.game.common.representation.state.GameStateCell;
import hu.kalmancheysandor.applications.dominions.apis.game.common.representation.state.GameStatePlayer;

import java.util.*;

import static hu.kalmancheysandor.applications.dominions.apis.util.validate.asserted.VariableAssert.assertIntNotNegative;
import static hu.kalmancheysandor.applications.dominions.apis.util.validate.asserted.VariableAssert.assertObjectIsNotNull;

public abstract class TOttoOperatorScoreEvaluator {

    protected int getMilitaryReserveCountOfAPlayer(GameState gameState, int observedPlayerKey) {
        // Parameter checking
        assertObjectIsNotNull(gameState, "GameState");
        assertIntNotNegative(observedPlayerKey, "ObservedPlayerKey");

        //
        GameStatePlayer playerObj = gameState.getOpponent(observedPlayerKey);
        assertObjectIsNotNull(playerObj, "PlayerObj");

        return playerObj.getReserveSize();
    }

    protected double reinforcementIncreaseNeed(GameState gameState, int observedPlayerKey) {
        // Parameter checking
        assertObjectIsNotNull(gameState, "GameState");
        assertIntNotNegative(observedPlayerKey, "ObservedPlayerKey");

        double  averageEnemyReserve =  averageEnemyReserve(gameState, observedPlayerKey);
        double  playerReserve = (double) getMilitaryReserveCountOfAPlayer(gameState, observedPlayerKey);

        return (50+((averageEnemyReserve-playerReserve)/2))/100;

    }

    private double averageEnemyReserve(GameState gameState, int observedPlayerKey) {
        // Parameter checking
        assertObjectIsNotNull(gameState, "GameState");
        assertIntNotNegative(observedPlayerKey, "ObservedPlayerKey");

        //
        int enemyCount = countActiveEnemies(gameState, observedPlayerKey);

        //
        GameStatePlayer playerObj = gameState.getOpponent(observedPlayerKey);
        assertObjectIsNotNull(playerObj, "PlayerObj");

        //
        if(!playerObj.isAlive()) {
            return 0.0;
        }
        else if(enemyCount == 0) {
            return 0.0;
        }

        //
        int sumOfMilitaryCount =0;
        for (int playerKey : gameState.getAlivePlayerIndexes()) {
            sumOfMilitaryCount += getMilitaryReserveCountOfAPlayer(gameState, playerKey);
        }
        return (double) sumOfMilitaryCount / enemyCount;
    }

    protected static int[] aliveEnemyPlayerIndexes(GameState gameState, int observedPlayerKey) {
        // Parameter checking
        assertObjectIsNotNull(gameState, "GameState");
        assertIntNotNegative(observedPlayerKey, "ObservedPlayerKey");

        //
        List<Integer> indexes = new ArrayList<>();
        for (int playerKey : gameState.getAlivePlayerIndexes()) {
            if (playerKey != observedPlayerKey) {
                indexes.add(playerKey);
            }
        }
        return indexes.stream().mapToInt(i -> i).toArray();
    }


    protected static int countPotentialAttackersAgainstACell(GameState gameState, int cellKey, int observedPlayerKey) {
        // Parameter checking
        assertObjectIsNotNull(gameState, "GameState");
        assertIntNotNegative(cellKey, "CellKey");
        assertIntNotNegative(observedPlayerKey, "ObservedPlayerKey");

        //
        if (!isPlayerAlive(gameState, observedPlayerKey)) {
            return 0;
        }

        // Counting that how many enemy player can attack that cell
        int[] aliveEnemyPlayerKeys = aliveEnemyPlayerIndexes(gameState, observedPlayerKey);
        int potentialAttackers = 0;
        for (int enemyKey : aliveEnemyPlayerKeys) {

            if(isCellAttackableForPlayer(gameState, cellKey, enemyKey)) {
                potentialAttackers++;
            }
        }
        return potentialAttackers;
    }







    private static boolean isCellAttackableForPlayer(GameState gameState, int cellKey, int observedPlayerKey) {
        // Parameter checking
        assertObjectIsNotNull(gameState, "GameState");
        assertIntNotNegative(cellKey, "CellKey");
        assertIntNotNegative(observedPlayerKey, "ObservedPlayerKey");

        //
        GameStateCell cellObj = gameState.getCell(cellKey);
        assertObjectIsNotNull(cellObj, "cellObj");

        //
        if(GameState.isCellInAttackZoneOfPlayer(gameState,  observedPlayerKey, cellKey)) {
            if (cellObj.isEmpty()) {
                return true;
            } else if (cellObj.getOccupierKey() != observedPlayerKey) {
                return true;
            }
        }
        return false;
    }

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














}
