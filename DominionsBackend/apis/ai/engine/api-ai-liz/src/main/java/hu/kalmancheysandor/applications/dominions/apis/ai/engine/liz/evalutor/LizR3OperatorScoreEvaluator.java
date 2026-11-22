package hu.kalmancheysandor.applications.dominions.apis.ai.engine.liz.evalutor;


import hu.kalmancheysandor.applications.dominions.apis.ai.engine.liz.evalution.LizOperatorScoreEvaluation;
import hu.kalmancheysandor.applications.dominions.apis.ai.engine.liz.interfaces.ILizEnemyDataItem;
import hu.kalmancheysandor.applications.dominions.apis.ai.engine.liz.interfaces.ILizEvaluationInput;
import hu.kalmancheysandor.applications.dominions.apis.ai.engine.liz.interfaces.ILizOperatorScoreEvaluation;
import hu.kalmancheysandor.applications.dominions.apis.ai.engine.liz.interfaces.ILizOperatorScoreEvaluator;
import hu.kalmancheysandor.applications.dominions.apis.ai.engine.liz.neural.LizNeuralNetworkModelInput;
import hu.kalmancheysandor.applications.dominions.apis.ai.engine.liz.neural.LizNeuralNetworkModelOutput;
import hu.kalmancheysandor.applications.dominions.apis.ai.neural.network.INeuralNetwork;
import hu.kalmancheysandor.applications.dominions.apis.game.common.representation.state.GameState;
import hu.kalmancheysandor.applications.dominions.apis.game.common.representation.state.GameStateCell;
import hu.kalmancheysandor.applications.dominions.apis.game.common.representation.state.GameStatePlayer;

import java.util.Map;

import static hu.kalmancheysandor.applications.dominions.apis.util.validate.asserted.VariableAssert.*;
import static hu.kalmancheysandor.applications.dominions.apis.util.validate.asserted.VariableAssert.assertIntIsInInterval;

public class LizR3OperatorScoreEvaluator implements ILizOperatorScoreEvaluator {

    @Override
    public ILizOperatorScoreEvaluation evaluate(ILizEvaluationInput input) {
        // Parameter checking
        assertObjectIsNotNull(input, "input");
        assertObjectIsNotNull(input.getGameState(), "input.GameState");
        assertIntNotNegative(input.getOperatorKey(), "input.OperatorKey");
        assertIntIsInInterval(input.getOperatorKey(), "input.operatorKey", 0, input.getGameState().getCells().length);// Here is an extra operator (skip), so "length-1" does not needs
        assertIntNotNegative(input.getObservedPlayerKey(), "input.ObservedPlayerKey");
        assertIntIsInInterval(input.getObservedPlayerKey(), "input.observedPlayerKey", 0, input.getGameState().getOpponents().length - 1);

        // Input to local variables
        GameState gameState = input.getGameState();
        int observedPlayerKey = input.getObservedPlayerKey();
        int operatorKey = input.getOperatorKey();
        Map<Integer, ILizEnemyDataItem> enemyDataList = input.getEnemyDataList();

        //
        GameStatePlayer playerObj = gameState.getOpponent(observedPlayerKey);
        assertObjectIsNotNull(playerObj, "PlayerObj");

        //
        double averageOperatorProbability = averageOpponentActionProbability(gameState, enemyDataList, operatorKey);
        System.out.println("Average operator probability for operator " + operatorKey + " is: " + averageOperatorProbability);
        //
        double output = averageOperatorProbability;
        if (operatorKey == 0) {    // Skip
            output = 1.0 - averageOperatorProbability;
        }
        System.out.println("Final score for operator " + operatorKey + " is: " + output);
        return new LizOperatorScoreEvaluation(output);
    }


    /// ///////////////////////////////////////////////////////////////////////////////////////
    /// ///////////////////////////////////////////////////////////////////////////////////////
    /// ///////////////////////////////////////////////////////////////////////////////////////
    private double averageOpponentActionProbability(GameState gameState,Map<Integer, ILizEnemyDataItem> enemyDataList, int operatorKey) {
        // Parameter checking
        assertMapNotEmpty(enemyDataList, "EnemyDataList");
        assertIntNotNegative(operatorKey, "OperatorKey");
        assertIntIsInInterval(operatorKey, "operatorKey", 0, gameState.getCells().length);// Here is an extra operator (skip), so "length-1" does not needs

        double sumOfOperatorProbability = 0.0;
        for (Map.Entry<Integer, ILizEnemyDataItem> entry : enemyDataList.entrySet()) {
            int enemyPlayerKey = entry.getValue().getPlayerKey();
            INeuralNetwork enemyNetwork = entry.getValue().getNeuralNetwork();

            //
            LizNeuralNetworkModelOutput modelOutputObj = enemyNetworkModelOutput(gameState,enemyNetwork , enemyPlayerKey);
            double[] modelOperatorProbability = modelOutputObj.getOperatorProbability();
            System.out.println("Operator probabilities for enemy player " + enemyPlayerKey + " are: " + java.util.Arrays.toString(modelOperatorProbability));
            double operatorUseProbability = modelOperatorProbability[operatorKey];
            System.out.println("Operator probability for operator " + operatorKey + " is: " + operatorUseProbability);

            //
            sumOfOperatorProbability += operatorUseProbability;
        }
        return sumOfOperatorProbability / enemyDataList.size();
    }

    private LizNeuralNetworkModelOutput enemyNetworkModelOutput(GameState gameState,INeuralNetwork enemyPlayerNeuralNetwork , int enemyPlayerKey) {
        int boardCellCount = gameState.getCellCount();

        // Raw response based on current situation
        LizNeuralNetworkModelInput networkInputData = generateNetworkModelInputFromGameState(gameState, enemyPlayerKey);
        System.out.println("Neural network input data for enemy player " + enemyPlayerKey + " is: " + networkInputData.toString());

        // Use network to calculate
        LizNeuralNetworkModelOutput modelOutputObj = (LizNeuralNetworkModelOutput) enemyPlayerNeuralNetwork.calculate(networkInputData);
        if (modelOutputObj == null) { // When there is no network file
            throw new RuntimeException("Neural network model output is null! This is unexpected!");
        }

        return modelOutputObj;
    }


    /**
     * In gameState an empty cell has occupierKey -1, and the first player has index 0
     *
     * @param gameState
     * @param observedPlayerKey
     * @return
     */
    private LizNeuralNetworkModelInput generateNetworkModelInputFromGameState(GameState gameState, int observedPlayerKey) {

        // Parameter checking
        if (!gameState.isOpponentExist(observedPlayerKey)) { // Just to check if the player is valid
            throw new IllegalArgumentException("Observed player key is invalid!");
        }

        // Initialise
        int allPlayerCount = gameState.getPlayerCount();
        int allCellCount = gameState.getCellCount();

        // Initialise
        int[] inputNode_cellOccupierIndex = new int[allCellCount];
        int[] inputNode_cellGarrisonSize = new int[allCellCount];
        int[] inputNode_playerTerritorySize = new int[allPlayerCount];
        int[] inputNode_playerReserveSize = new int[allPlayerCount];
        boolean[] inputNode_playerIsAlive = new boolean[allPlayerCount];

        //
        int cellIndex = 0;
        for (GameStateCell cell : gameState.getCells()) {

            // Index alterations
            int swappedPlayerIndex = swapObservedPlayerIndexToZero(observedPlayerKey, cell.getOccupierKey()); // After swap: empty cell is still -1, observedPlayer:0 other player higher than 0
            int shiftedPlayerIndex = swappedPlayerIndex + 1; // Reason: in NN -1 means a not working node. so after shift: empty cell is:0, observed player is:1 other player is higher than 1
            //
            inputNode_cellOccupierIndex[cellIndex] = shiftedPlayerIndex;
            inputNode_cellGarrisonSize[cellIndex] = cell.getDefendingTroopSize();
            cellIndex++;
        }

        //
        for (int playerIndex = 0; playerIndex < allPlayerCount; playerIndex++) {    //
            // Index alterations
            int swappedPlayerIndex = swapObservedPlayerIndexToZero(observedPlayerKey, playerIndex);

            // Swap is needed due to remain order wise(means: first node is for observed player)
            // Do not shift the indexes
            inputNode_playerTerritorySize[swappedPlayerIndex] = gameState.getOccupiedCellsCountOfPlayer(playerIndex);// Index on right side have to be original value in order to access the original state of player
            inputNode_playerReserveSize[swappedPlayerIndex] = getPlayerReserveSize(gameState, playerIndex);// Index on right side have to be original value in order to access the original state of player
            inputNode_playerIsAlive[swappedPlayerIndex] = gameState.getOpponent(playerIndex).isAlive();// Index on right side have to be original value in order to access the original state of player
        }


        return LizNeuralNetworkModelInput.builder()
                .cellOccupierIndex(inputNode_cellOccupierIndex)
                .cellGarrisonSize(inputNode_cellGarrisonSize)
                .playerTerritorySize(inputNode_playerTerritorySize)
                .playerReserveSize(inputNode_playerReserveSize)
                .playerIsAlive(inputNode_playerIsAlive)
                .build();
    }


    private int swapObservedPlayerIndexToZero(int supportedPlayerIndex, int currentPlayerIndex) {

        if (currentPlayerIndex == supportedPlayerIndex) {
            return 1;
        } else if (currentPlayerIndex == 1) {
            return supportedPlayerIndex;
        }
        return currentPlayerIndex;
    }

    protected static int getPlayerReserveSize(GameState gameState, int playerKey) {
        return gameState.getOpponent(playerKey).getReserveSize();
    }
}
