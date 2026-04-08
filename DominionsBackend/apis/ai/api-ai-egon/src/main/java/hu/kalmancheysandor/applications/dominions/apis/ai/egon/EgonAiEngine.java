package hu.kalmancheysandor.applications.dominions.apis.ai.egon;

import hu.kalmancheysandor.applications.dominions.apis.ai.common.AiDecisionContext;
import hu.kalmancheysandor.applications.dominions.apis.ai.common.AiDecisionResult;
import hu.kalmancheysandor.applications.dominions.apis.ai.common.IAiEngine;
import hu.kalmancheysandor.applications.dominions.apis.ai.common.TAiEngine;
import hu.kalmancheysandor.applications.dominions.apis.game.common.representation.state.GameState;
import hu.kalmancheysandor.applications.dominions.apis.game.common.representation.state.GameStateCell;

import java.util.*;

public class EgonAiEngine extends TAiEngine implements IAiEngine {

    @Override
    public AiDecisionResult makeDecision(AiDecisionContext context) {
        // Preparation
        int myAttackPower = calculatePlayerMaxAttackPower(context.getGameState(), context.getYourPlayerKey());

        // Make decision
        Integer decisionCell = makeADecision(context.getGameState(), context.getYourPlayerKey());
        System.out.println("Decision cell:" + decisionCell);
        System.out.println("myAttackPower:" + myAttackPower);
        if (decisionCell != null) {
            return new AiDecisionResult(decisionCell, myAttackPower);
        }
        return new AiDecisionResult(0, 0);
    }

    private Integer makeADecision(GameState gameState, int supportedPlayerKey) {
        System.out.println("---------------makeADecision---------------");

        // Preparation
        int myAttackPower = calculatePlayerMaxAttackPower(gameState, supportedPlayerKey);
        Map<Integer, Set<Integer>> riskGroups = new TreeMap<>(determineRiskGroups(gameState, supportedPlayerKey));
        Set<Integer> myAttackZoneKeys = collectCellKeysOfCurrentAttackZoneOfPlayer(gameState, supportedPlayerKey);

        //
        Set<Integer> threatenedCellKeys;
        Integer threateningEnemyCount;
        Set<Integer> observedAttackZoneKeys;
        GameStateCell observedCell;
        for (Map.Entry<Integer, Set<Integer>> entry : riskGroups.entrySet()) { // Increasing possibility of conflict
            threateningEnemyCount = entry.getKey();
            threatenedCellKeys = entry.getValue();
            System.out.println("AttackZoneKeys[ThreateningEnemyCount:" + threateningEnemyCount + " db]" + myAttackZoneKeys);
            System.out.println("RiskGroup[ThreateningEnemyCount:" + threateningEnemyCount + " db]" + threatenedCellKeys);

            // Creates the intersection of the possible attack zones and the cell keys of related risk group.
            // Any risk score lower than the current one is no longer considered potential.
            observedAttackZoneKeys = new HashSet<>(myAttackZoneKeys);
            observedAttackZoneKeys.retainAll(threatenedCellKeys);

            System.out.println("Intercessed[ThreateningEnemyCount:" + threateningEnemyCount + " db]" + observedAttackZoneKeys);


            // Option1: Empty ones - Trys to find an empty cell in the intersection. If there is no one, then check the following option.
            // Trys to find: The first cell
            for (int anObservedAttackZoneKeys : observedAttackZoneKeys) {
                observedCell = gameState.getCell(anObservedAttackZoneKeys);
                if (observedCell.isEmpty()) {
                    System.out.println("-Found:Empty");
                    return anObservedAttackZoneKeys;
                }
            }

            // Option2: Weaker ones - Trys to find a weaker cell in the intersection than attacking troops. If there is no one, then check the following option.
            // Trys to find: the most weak cell
            Integer mostWeakerCellKey = null;
            Integer lowestWeakness = Integer.MAX_VALUE;
            for (int anObservedAttackZoneKeys : observedAttackZoneKeys) {
                observedCell = gameState.getCell(anObservedAttackZoneKeys);
                if (!observedCell.isEmpty() && observedCell.getDefendingTroopSize() < myAttackPower) {
                    if (observedCell.getDefendingTroopSize() < lowestWeakness) {
                        lowestWeakness = observedCell.getDefendingTroopSize();
                        mostWeakerCellKey = anObservedAttackZoneKeys;
                    }
                }
            }
            if (mostWeakerCellKey != null) {
                System.out.println("-Found:Weaker");
                return mostWeakerCellKey;
            }

            // Option3: Equal ones - Trys to find an equally strong cell in the intersection than attacking troops. If there is no one, then check the following option.
            // Trys to find: The first cell
            for (int anObservedAttackZoneKeys : observedAttackZoneKeys) {
                observedCell = gameState.getCell(anObservedAttackZoneKeys);
                if (!observedCell.isEmpty() && observedCell.getDefendingTroopSize() == myAttackPower) {
                    System.out.println("-Found:Equal");
                    return anObservedAttackZoneKeys;
                }
            }

            // Option4: Stronger ones - - Trys to find a stronger cell in the intersection than attacking troops. If there is no one, then check the following option.
            // Trys to find the most strong cell
            Integer lessStrongerCellKey = null;
            Integer lowestStrength = Integer.MAX_VALUE;
            for (int anObservedAttackZoneKeys : observedAttackZoneKeys) {
                observedCell = gameState.getCell(anObservedAttackZoneKeys);
                if (!observedCell.isEmpty() && observedCell.getDefendingTroopSize() > myAttackPower) {
                    if (observedCell.getDefendingTroopSize() < lowestStrength) {
                        lowestStrength = observedCell.getDefendingTroopSize();
                        lessStrongerCellKey = anObservedAttackZoneKeys;
                    }
                }
            }
            if (lessStrongerCellKey != null) {
                System.out.println("-Found:Stronger");
                return lessStrongerCellKey;
            }

        }

        System.out.println("-Found:Nothing");
        return null;
    }

    private int countOfPossibleEnemy(GameState gameState, int observedCellKey, int supportedPlayerKey) {
        Set<Integer> aliveEnemyPlayerKeys = collectAllAlivePlayerKeys(gameState);
        aliveEnemyPlayerKeys.remove(supportedPlayerKey);

        int count = 0;
        for (Integer enemyPlayerKey : aliveEnemyPlayerKeys) {
            if (isCellInAttackZoneOfPlayer(gameState, enemyPlayerKey, observedCellKey)) {
                count++;
            }
        }
        return count;
    }

    private Map<Integer, Set<Integer>> determineRiskGroups(GameState gameState, int supportedPlayerKey) {

        Map<Integer, Set<Integer>> riskGroups = new TreeMap<>();

        Integer landIndex = 0;
        int possibleEnemyCount;
        for (GameStateCell cell : gameState.getCells()) {
            possibleEnemyCount = countOfPossibleEnemy(gameState, landIndex, supportedPlayerKey);

            if (riskGroups.containsKey(possibleEnemyCount)) {
                Set<Integer> keyList = riskGroups.get(possibleEnemyCount);
                keyList.add(landIndex);
            } else {
                Set<Integer> keyList = new HashSet<>();
                keyList.add(landIndex);
                riskGroups.put(possibleEnemyCount, keyList);
            }

            landIndex++;
        }
        return riskGroups;
    }

}
