package hu.kalmancheysandor.applications.dominions.apis.ai.hugo;


import hu.kalmancheysandor.applications.dominions.apis.ai.common.TAiEngine;
import hu.kalmancheysandor.applications.dominions.apis.game.common.representation.action.GameAction;
import hu.kalmancheysandor.applications.dominions.apis.game.common.representation.machine.GameStateMachine;
import hu.kalmancheysandor.applications.dominions.apis.game.common.representation.state.GameState;

import java.util.*;

public abstract class THugoAIEngine extends TAiEngine {

    protected GameStateMachine gameStateMachine;


    ///////////////////////////////////////////////////////////////////////////////////////////
    ///// [Combination methods] ///////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////
    protected GameState applyOneCombinationOnGameState(GameState initialGameState, Map<Integer, Integer> combination) {

        // Initialise state-machine
        if (this.gameStateMachine == null) {
            this.gameStateMachine = new GameStateMachine();
        }

        // Collect all intentions
        Set<GameAction> actions = new HashSet<>();
        Integer targetCellKey;
        Integer playerKey;
        Integer troopSize;
        for (Map.Entry<Integer, Integer> entry : combination.entrySet()) {
            playerKey = entry.getKey();
            targetCellKey = entry.getValue();
            troopSize = getPlayerReserveSize(initialGameState, playerKey);
            troopSize = (troopSize > TROOPS_SIZE_MAX) ? TROOPS_SIZE_MAX : troopSize;
            if (targetCellKey == null) {//When reserve action
                troopSize = 0;
            }
            actions.add(new GameAction(playerKey, targetCellKey, troopSize));
        }

        // Generate new game-state based on the intentions
        return gameStateMachine.doSteps(actions, GameState.deepCopy(initialGameState));
    }

    protected Map<Integer, Set<Integer>> collectAvailableStepsOfAllPlayer(GameState gameState) {
        // All players key
        Set<Integer> playerKeys = collectAllAlivePlayerKeys(gameState);

        //
        Map<Integer, Set<Integer>> playersAttackZoneList = new HashMap<>();
        Set<Integer> steps;
        for (Integer playerKey : playerKeys) {
            steps = new HashSet<>(collectCellKeysOfCurrentAttackZoneOfPlayer(gameState, playerKey));
            steps.add(null);        // Reserve step
            playersAttackZoneList.put(playerKey, steps);
        }

        return playersAttackZoneList;
    }

    protected Map<Integer, List<Map<Integer, Integer>>> generateCombinationGroups(GameState gameState, int supportedPlayerKey) {
        // Generate all players all available step-combination
        List<Map<Integer, Integer>> allStepCombinationsList = generateAllPossibleStepCombinations(gameState);

        Integer targetedCellKey;
        List<Map<Integer, Integer>> combinationListInAGroup;
        Map<Integer, List<Map<Integer, Integer>>> stepGroups = new HashMap<>();
        HeuristicData hData;
        for (Map<Integer, Integer> combinationItem : allStepCombinationsList) {
            //System.out.println("CombinationItem:"+combinationItem);
            if (!combinationItem.containsKey(supportedPlayerKey)) {
                //throw new IllegalPointOfExecution("Missing key("+supportedPlayerKey+") of supported player");
            }

            // Determine group-key
            targetedCellKey = combinationItem.get(supportedPlayerKey);

            // Add to related group
            if (!stepGroups.containsKey(targetedCellKey)) {
                combinationListInAGroup = new ArrayList<>();
                combinationListInAGroup.add(combinationItem);
                stepGroups.put(targetedCellKey, combinationListInAGroup);
            } else {
                combinationListInAGroup = stepGroups.get(targetedCellKey);
                combinationListInAGroup.add(combinationItem);
            }
        }
        return stepGroups;
    }

    /**
     * Minden jatekos minden tamado cellaba torteno lepese altal letrejott osszes kombinaciot listazza
     *
     * @param gameState
     * @return
     */
    protected List<Map<Integer, Integer>> generateAllPossibleStepCombinations(GameState gameState) {
        List<Map<Integer, Integer>> combinationsList = new ArrayList<>();
        Set<Integer> playerKeys = collectAllAlivePlayerKeys(gameState);

        Map<Integer, Set<Integer>> playersAvailableSteps = collectAvailableStepsOfAllPlayer(gameState);

        recursive(0, playersAvailableSteps, playerKeys, new HashMap<>(), combinationsList);

        return combinationsList;
    }

    protected static void recursive(int observedPlayerKey, Map<Integer, Set<Integer>> playersAvailableSteps, Set<Integer> playerKeys, Map<Integer, Integer> stepsOfOneTurn, List<Map<Integer, Integer>> combinationsList) {

        Set<Integer> observedAvailableSteps = playersAvailableSteps.get(observedPlayerKey);
        int nextPlayerKey = observedPlayerKey + 1;
        for (Integer targetedCellKey : observedAvailableSteps) {
            stepsOfOneTurn.put(observedPlayerKey, targetedCellKey);
            if (playerKeys.contains(nextPlayerKey)) {
                recursive(nextPlayerKey, playersAvailableSteps, playerKeys, stepsOfOneTurn, combinationsList);
            } else { // att bottom deep
                combinationsList.add(new HashMap<>(stepsOfOneTurn));
            }
        }
    }

    protected Map<Integer, List<GameState>> phase2(GameState gameState, Map<Integer, List<Map<Integer, Integer>>> combinationsGroups) {
        Integer combinationGroupKey;
        List<Map<Integer, Integer>> combinationListInAGroup;
        Map<Integer, List<GameState>> gameStateListGroups = new HashMap<>();
        for (Map.Entry<Integer, List<Map<Integer, Integer>>> combinationGroupEntry : combinationsGroups.entrySet()) {
            // Initialisation
            combinationGroupKey = combinationGroupEntry.getKey();
            combinationListInAGroup = combinationGroupEntry.getValue();

            //
//            System.out.println("...........");
//            System.out.println("StepGroupKey:" + combinationGroupKey);
            List<GameState> gameStateList = new ArrayList<>();
            for (Map<Integer, Integer> stepCombinationItem : combinationListInAGroup) {
//                System.out.println("Step:" + stepCombinationItem);
                gameStateList.add(applyOneCombinationOnGameState(gameState, stepCombinationItem));
            }
            gameStateListGroups.put(combinationGroupKey, gameStateList);
        }

        return gameStateListGroups;
    }

}
