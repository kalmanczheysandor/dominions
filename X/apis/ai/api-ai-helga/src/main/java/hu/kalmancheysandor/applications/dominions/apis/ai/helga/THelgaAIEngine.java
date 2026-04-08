package hu.kalmancheysandor.applications.dominions.apis.ai.helga;


import hu.kalmancheysandor.applications.dominions.apis.ai.common.AiDecisionContext;
import hu.kalmancheysandor.applications.dominions.apis.ai.common.TAiEngine;
import hu.kalmancheysandor.applications.dominions.apis.ai.common.exception.AiException;
import hu.kalmancheysandor.applications.dominions.apis.ai.neural.INeuralAiEngine;
import hu.kalmancheysandor.applications.dominions.apis.ai.neural.INeuralInputData;
import hu.kalmancheysandor.applications.dominions.apis.game.common.representation.action.GameAction;
import hu.kalmancheysandor.applications.dominions.apis.game.common.representation.machine.GameStateMachine;
import hu.kalmancheysandor.applications.dominions.apis.game.common.representation.state.GameState;
import hu.kalmancheysandor.applications.dominions.apis.game.common.representation.state.GameStateCell;
import org.neuroph.core.data.DataSetRow;

import java.util.*;

public abstract class THelgaAIEngine extends TAiEngine implements INeuralAiEngine {

    protected GameStateMachine gameStateMachine;

    private HelgaNeuralNetwork neuralNetwork;

    public THelgaAIEngine() {
        this.neuralNetwork = new HelgaNeuralNetwork(new HelgaNeuralNetwork.Configuration()
            .setEnemiesCountMin(ENEMIES_COUNT_MIN)
            .setEnemiesCountMax(ENEMIES_COUNT_MAX)
            .setOwnersCountMin(OWNER_RANKS_INDEX_MIN)
            .setOwnersCountMax(OWNER_RANKS_INDEX_MAX)
            .setDefendersSizeMin(DEFENDERS_COUNT_MIN)
            .setDefendersSizeMax(DEFENDERS_COUNT_MAX)
            .setAttackPowerMin(RESERVE_SIZE_MIN)
            .setAttackPowerMax(RESERVE_SIZE_MAX)
        );
    }

    @Override
    public void trainIt(String userUuid, List<INeuralInputData> trainingDataList) {
        System.out.println("HELGA AI Start training...");
        System.out.println("Player code: " + userUuid);
        System.out.println("Training data list:" + trainingDataList.size());

        // Execution
        neuralNetwork.train(userUuid, trainingDataList);
    }

    @Override
    public void testIt(String userUuid, List<INeuralInputData> testingDataList) {
        System.out.println("HELGA AI Start checking...");
        System.out.println("Player code: " + userUuid);
    }


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

    private DataSetRow buildDataSetRowFromGameState(GameState gameState, int supportedPlayerKey) {
        int reserveSize = getPlayerReserveSize(gameState, supportedPlayerKey);
        int attackPower = reserveSize;
        if(attackPower>RESERVE_SIZE_MAX) {
            attackPower = RESERVE_SIZE_MAX;
        }
        int enemyPlayerCount = contOfAlivePlayers(gameState) - 1;
        if (enemyPlayerCount < 0) {
            throw new AiException("Unexpected value of enemyPlayerCount:" + enemyPlayerCount);
        }

        int[] cellOwnersList = new int[gameState.getCellCount()];
        int[] cellDefenderSizeList = new int[gameState.getCellCount()];

        int cellIndex = 0;
        for (GameStateCell cell : gameState.getCells()) {
            cellOwnersList[cellIndex] = gameState.getCell(cellIndex).getOccupierKey();
            cellDefenderSizeList[cellIndex] = gameState.getCell(cellIndex).getDefendingTroopSize();
            cellIndex++;
        }

        HelgaInputData trainingData = new HelgaInputData();
        trainingData.setAttackPower(attackPower);
        trainingData.setEnemiesCount(enemyPlayerCount);
        trainingData.setCellOwners(cellOwnersList);
        trainingData.setCellDefendersSize(cellDefenderSizeList);

        return new DataSetRow(neuralNetwork.buildInputVector(trainingData));
    }

    protected Map<Integer, Set<Integer>> collectAvailableStepsOfAllPlayer(GameState gameState, int supportedPlayerKey, Map<Integer, AiDecisionContext.Player> players) {
        System.out.println("----------------collectAvailableStepsOfAllPlayer--------------------------");


        // All players key
        Set<Integer> playerKeys = collectAllAlivePlayerKeys(gameState);

        //
        Map<Integer, Set<Integer>> playersAttackZoneList = new HashMap<>();
        Set<Integer> steps;
        for (Integer playerKey : playerKeys) {
            steps = new HashSet<>(collectCellKeysOfCurrentAttackZoneOfPlayer(gameState, playerKey));
            steps.add(null);        // Reserve step
            if (playerKey != supportedPlayerKey) {
                System.out.println("--Enemey: " + playerKey + " >>> " + players.get(playerKey).getUserUuid());

                DataSetRow modelInput = buildDataSetRowFromGameState(gameState, supportedPlayerKey);
                Set<Integer> potentialHits = neuralNetwork.calculate(players.get(playerKey).getUserUuid(), modelInput);
                potentialHits.add(null); // TODO: mivel nem megfeleloen mukodik a halo
                steps.retainAll(potentialHits);
            } else {
                System.out.println("--You: " + playerKey + " >>> " + players.get(playerKey).getUserUuid());
            }
            System.out.println("STEPS:" + steps);
            playersAttackZoneList.put(playerKey, steps);
        }

        return playersAttackZoneList;
    }

    protected Map<Integer, List<Map<Integer, Integer>>> generateCombinationGroups(GameState gameState, int supportedPlayerKey, Map<Integer, AiDecisionContext.Player> players) {
        // Generate all players all available step-combination
        List<Map<Integer, Integer>> allStepCombinationsList = generateAllPossibleStepCombinations(gameState, supportedPlayerKey, players);

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
    protected List<Map<Integer, Integer>> generateAllPossibleStepCombinations(GameState gameState, int supportedPlayerKey, Map<Integer, AiDecisionContext.Player> players) {
        List<Map<Integer, Integer>> combinationsList = new ArrayList<>();
        Set<Integer> playerKeys = collectAllAlivePlayerKeys(gameState);

        Map<Integer, Set<Integer>> playersAvailableSteps = collectAvailableStepsOfAllPlayer(gameState, supportedPlayerKey, players);

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
        System.out.println("<<<<<      Phase2           >>>>>");


        Integer combinationGroupKey;
        List<Map<Integer, Integer>> combinationListInAGroup;
        Map<Integer, List<GameState>> gameStateListGroups = new HashMap<>();
        for (Map.Entry<Integer, List<Map<Integer, Integer>>> combinationGroupEntry : combinationsGroups.entrySet()) {
            // Initialisation
            combinationGroupKey = combinationGroupEntry.getKey();
            combinationListInAGroup = combinationGroupEntry.getValue();

            //
            System.out.println("...........");
            System.out.println("StepGroupKey:" + combinationGroupKey);
            List<GameState> gameStateList = new ArrayList<>();
            GameState newState;
            for (Map<Integer, Integer> stepCombinationItem : combinationListInAGroup) {
                newState = applyOneCombinationOnGameState(gameState, stepCombinationItem);
                gameStateList.add(newState);
                if (newState.isFinalState()) {
                    System.out.println("Step:" + stepCombinationItem+" >> FinalState");
                } else {
                    System.out.println("Step:" + stepCombinationItem);
                }
            }
            gameStateListGroups.put(combinationGroupKey, gameStateList);
        }

        return gameStateListGroups;
    }

}
