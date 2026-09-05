package hu.kalmancheysandor.applications.dominions.apis.ai.engine.otto;


import hu.kalmancheysandor.applications.dominions.apis.ai.defensive.TDefensiveEngine;
import hu.kalmancheysandor.applications.dominions.apis.ai.general.common.AiDecisionContext;
import hu.kalmancheysandor.applications.dominions.apis.ai.general.common.AiDecisionResult;
import hu.kalmancheysandor.applications.dominions.apis.game.common.representation.state.GameState;
import hu.kalmancheysandor.applications.dominions.apis.game.common.representation.state.GameStateCell;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class OttoAiEngine extends TDefensiveEngine {

    @Override
    public AiDecisionResult makeDecision(AiDecisionContext context) {
       return super.makeDecision(context);
    }

    @Override
    protected Map<Double, Set<Integer>> buildCellRankGroups(GameState gameState, int supportedPlayerKey, Map<Integer, AiDecisionContext.Player> enemyPlayers) {

        Map<Double, Set<Integer>> cellRankGroups = new TreeMap<>();
        int observedCellIndex = 0;
        int occupierKey;
        for (GameStateCell cell : gameState.getCells()) {

            // Because the aim is to determine the next step of supported player, so its cells are not counted
            occupierKey = cell.getOccupierKey();
            if (occupierKey == supportedPlayerKey) {
                continue;
            }

            //
            Double cellAttackScore = (double) collectEnemyPlayerCountInTheNeighbour(gameState, observedCellIndex);

            // Gather cell keys into cellAttackScore-value groups
            if (cellRankGroups.containsKey(cellAttackScore)) { // When the group is already defined
                Set<Integer> keyList = cellRankGroups.get(cellAttackScore);
                keyList.add(observedCellIndex);
                cellRankGroups.put(cellAttackScore, keyList);
            } else { // When the group is not defined yet
                Set<Integer> keyList = new HashSet<>();
                keyList.add(observedCellIndex);
                cellRankGroups.put(cellAttackScore, keyList);
            }

            observedCellIndex++;
        }

        return cellRankGroups;
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

}
