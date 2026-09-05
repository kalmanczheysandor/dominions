package hu.kalmancheysandor.applications.dominions.apis.ai.defensive;

import hu.kalmancheysandor.applications.dominions.apis.ai.general.common.AiDecisionContext;
import hu.kalmancheysandor.applications.dominions.apis.ai.general.common.AiDecisionResult;
import hu.kalmancheysandor.applications.dominions.apis.ai.general.common.TAiEngine;
import hu.kalmancheysandor.applications.dominions.apis.game.common.representation.state.GameState;
import hu.kalmancheysandor.applications.dominions.apis.game.common.representation.state.GameStateCell;

import java.util.*;

public abstract class TDefensiveEngine extends TAiEngine {

    protected abstract Map<Double, Set<Integer>> buildCellRankGroups(GameState gameState, int supportedPlayerKey, Map<Integer, AiDecisionContext.Player> enemyPlayers);

    @Override
    public AiDecisionResult makeDecision(AiDecisionContext context) {
        // Preparation
        int myAttackPower = calculatePlayerMaxAttackPower(context.getGameState(), context.getYourPlayerKey());

        // Make decision
        Integer decisionCell = chooseAnAction(context.getGameState(), context.getYourPlayerKey(), context.getPlayers());
        System.out.println("Decision cell:" + decisionCell);
        System.out.println("myAttackPower:" + myAttackPower);
        if (decisionCell != null) {
            return new AiDecisionResult(decisionCell, myAttackPower);
        }
        return new AiDecisionResult(0, 0);
    }


    private Integer chooseAnAction(GameState gameState, int supportedPlayerKey, Map<Integer, AiDecisionContext.Player> players) {
        System.out.println("---------------chooseAnAction---------------");

        // Enemy players (by removing the supported player)
        Map<Integer, AiDecisionContext.Player> enemyPlayers = new HashMap<>(players);
        enemyPlayers.remove(supportedPlayerKey);

        // Preparation
        int attackPowerOfSupportedPlayer = calculatePlayerMaxAttackPower(gameState, supportedPlayerKey);
        Set<Integer> keysOfAttackZoneOfSupportedPlayer = collectCellKeysOfCurrentAttackZoneOfPlayer(gameState, supportedPlayerKey);
        Map<Double, Set<Integer>> rankGroups = new TreeMap<>(Comparator.naturalOrder()); // Providing increasing order of keys
        rankGroups.putAll(buildCellRankGroups(gameState, supportedPlayerKey, enemyPlayers));

        //
        Set<Integer> possibleConflictCellKeys;
        Set<Integer> observedAttackZoneKeys;
        Integer foundKey = null;
        for (Map.Entry<Double, Set<Integer>> entry : rankGroups.entrySet()) { // Increasing possibility of conflict
            possibleConflictCellKeys = entry.getValue();

            // Creates the intersection of the possible attack zones and the cell keys of related conflict group.
            // Any conflict score lower than the current one is no longer considered potential.
            observedAttackZoneKeys = new HashSet<>(keysOfAttackZoneOfSupportedPlayer);
            observedAttackZoneKeys.retainAll(possibleConflictCellKeys);

            // Option1: Empty ones - Tries to find an empty cell in the intersection. If there is no one, then check the following option.
            if (foundKey == null) {
                foundKey = findTheFirstEmptyCell(gameState, observedAttackZoneKeys);
            }

            // Option2: Weaker ones - Tries to find a weaker cell in the intersection than attacking troops. If there is no one, then check the following option.
            if (foundKey == null) {
                foundKey = findTheFirstMostWeakCell(gameState, observedAttackZoneKeys, attackPowerOfSupportedPlayer);
            }

            // Option3: Equal ones - Tries to find an equally strong cell in the intersection than attacking troops. If there is no one, then check the following option.
            if (foundKey == null) {
                foundKey = findTheFirstEqualCell(gameState, observedAttackZoneKeys, attackPowerOfSupportedPlayer);
            }


            // Option4: Stronger ones - Tries to find a stronger cell in the intersection than attacking troops. If there is no one, then check the following option.
            if (foundKey == null) {
                foundKey = findTheFirstLessStrongCell(gameState, observedAttackZoneKeys, attackPowerOfSupportedPlayer);
            }
        }
        return null;
    }


    private Integer findTheFirstEmptyCell(GameState gameState, Set<Integer> observedAttackZoneKeys) {
        GameStateCell observedCell;
        for (int anObservedAttackZoneKeys : observedAttackZoneKeys) {
            observedCell = gameState.getCell(anObservedAttackZoneKeys);
            if (observedCell.isEmpty()) {
                return anObservedAttackZoneKeys;
            }
        }
        return null;
    }

    private Integer findTheFirstEqualCell(GameState gameState, Set<Integer> observedAttackZoneKeys, int myAttackPower) {
        GameStateCell observedCell;
        for (int anObservedAttackZoneKeys : observedAttackZoneKeys) {
            observedCell = gameState.getCell(anObservedAttackZoneKeys);
            if (!observedCell.isEmpty() && observedCell.getDefendingTroopSize() == myAttackPower) {
                return anObservedAttackZoneKeys;
            }
        }
        return null;
    }

    private Integer findTheFirstMostWeakCell(GameState gameState, Set<Integer> observedAttackZoneKeys, int myAttackPower) {
        GameStateCell observedCell;
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
        return mostWeakerCellKey;
    }


    private Integer findTheFirstLessStrongCell(GameState gameState, Set<Integer> observedAttackZoneKeys, int myAttackPower) {
        GameStateCell observedCell;
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
        return lessStrongerCellKey;
    }
}
