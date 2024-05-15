package hu.kalmanczheysandor.application.dominion.game.common;

import hu.kalmanczheysandor.application.dominion.ai.basic.BasicEngine;
import hu.kalmanczheysandor.application.dominion.ai.common.AiRequest;
import hu.kalmanczheysandor.application.dominion.ai.common.AiResponse;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GameTest {
    private final static int player0Key = 0;
    private final static int player1Key = 1;
    private final static int player2Key = 2;
    private final static int player3Key = 3;
    private final static int player4Key = 4;

    private final static int cell1Key = 0;
    private final static int cell2Key = 1;
    private final static int cell3Key = 2;
    private final static int cell4Key = 3;

    private static void println(String s) {
        System.out.println(s);
    }

    private static void testDoughnutAttackRange_forFail() {
        GameState gameState = new GameState(5, 4);

        gameState.getCells()[cell2Key].setOccupierKey(player1Key);
        gameState.getCells()[cell2Key].setDefendingTroopSize(40);

        gameState.getCells()[cell3Key].setOccupierKey(player1Key);
        gameState.getCells()[cell3Key].setDefendingTroopSize(10);

        gameState.getCells()[cell4Key].setOccupierKey(player3Key);
        gameState.getCells()[cell4Key].setDefendingTroopSize(1);


        GameEngine engine = new GameEngine(gameState);


        Set<GameEngine.Action> actionGroup = new HashSet<>();
        actionGroup.add(new GameEngine.Action(player1Key, cell4Key, 10));

        GameState state = engine.doAction(actionGroup);


        System.out.println(state);
    }

    private static void testDoughnutAttackRange_forPass() {
        GameState gameState = new GameState(5, 4);

        gameState.getCells()[cell2Key].setOccupierKey(player1Key);
        gameState.getCells()[cell2Key].setDefendingTroopSize(40);

        gameState.getCells()[cell3Key].setOccupierKey(player1Key);
        gameState.getCells()[cell3Key].setDefendingTroopSize(10);


        GameState state;
        Set<GameEngine.Action> actionGroup;
        GameEngine engine = new GameEngine(gameState);

        actionGroup = new HashSet<>();
        actionGroup.add(new GameEngine.Action(player1Key, cell1Key, 1));
        state = engine.doAction(actionGroup);
        System.out.println(state);


        actionGroup = new HashSet<>();
        actionGroup.add(new GameEngine.Action(player1Key, cell4Key, 2));
        state = engine.doAction(actionGroup);

        System.out.println(state);
    }

    private static void testDominionDemolish() {
        GameState gameState = new GameState(5, 4);

        gameState.getOpponents()[player0Key].setAlive(false);
        gameState.getOpponents()[player3Key].setAlive(false);
        gameState.getOpponents()[player4Key].setAlive(false);

        gameState.getCells()[cell1Key].setOccupierKey(player1Key);
        gameState.getCells()[cell1Key].setDefendingTroopSize(40);

        gameState.getCells()[cell2Key].setOccupierKey(player2Key);
        gameState.getCells()[cell2Key].setDefendingTroopSize(40);

        gameState.getCells()[cell3Key].setOccupierKey(player2Key);
        gameState.getCells()[cell3Key].setDefendingTroopSize(10);

        gameState.getCells()[cell4Key].setOccupierKey(player2Key);
        gameState.getCells()[cell4Key].setDefendingTroopSize(10);

        GameState state;
        Set<GameEngine.Action> actionGroup;
        GameEngine engine = new GameEngine(gameState);

        actionGroup = new HashSet<>();
        //actionGroup.add(new GameEngine.Action(player1Key,cell1Key,1));
        state = engine.doAction(actionGroup);
        System.out.println(state);

        if (engine.isEndOfGame()) {
            System.out.println("END OF GAME");
        }
    }

    private static void testAi() {
        GameState gameState = new GameState(5, 4);

        gameState.getOpponents()[player0Key].setAlive(false);
        gameState.getOpponents()[player3Key].setAlive(false);
        gameState.getOpponents()[player4Key].setAlive(false);

        gameState.getOpponents()[player1Key].setReserveSize(100);


        gameState.getCells()[cell1Key].setOccupierKey(player1Key);
        gameState.getCells()[cell1Key].setDefendingTroopSize(40);

        gameState.getCells()[cell4Key].setOccupierKey(player2Key);
        gameState.getCells()[cell4Key].setDefendingTroopSize(20);

        Set<GameEngine.Action> actionGroup;
        GameEngine engine = new GameEngine(gameState);
        GameState state = engine.getGameState();
        BasicEngine ai1 = new BasicEngine();
        BasicEngine ai2 = new BasicEngine();


        println(state.toString());

        int turn = 0;
        do {
            println("-----------<TURN:" + turn + ">----------");
            if (engine.isEndOfGame()) {
                println("END OF GAME");
                break;
            }


            actionGroup = new HashSet<>();

            AiResponse response1 = ai2.generateResponse(convert(player1Key, state));
            AiResponse response2 = ai2.generateResponse(convert(player2Key, state));

            println("Player1Response:"+response1);
            println("Player2Response:"+response2);

            actionGroup.add(new GameEngine.Action(player1Key, response1.getTargetCellKey(), response1.getTroopSize()));
            actionGroup.add(new GameEngine.Action(player2Key, response2.getTargetCellKey(), response2.getTroopSize()));

            engine.doAction(actionGroup);
            state = engine.getGameState();
            println(state.toString());

            if (turn >= 100) {
                println("!!! <TO MUCH LOOP> !!!");
                break;
            }
            turn++;
        } while (true);


//        actionGroup = new HashSet<>();
//        //actionGroup.add(new GameEngine.Action(player1Key,cell1Key,1));
//        state = engine.doAction(actionGroup);
//        System.out.println(state);
//
//        if (engine.isEndOfGame()) {
//            System.out.println("END OF GAME");
//        }
    }


    private static Set<Integer> neighbours(boolean[][] matrix, int cellKey) {
        Set<Integer> output = new HashSet<>();
        boolean[] row = matrix[cellKey];
        for (int neighbourKey = 0; neighbourKey < row.length; neighbourKey++) {
            if (row[neighbourKey] == true) {
                output.add(neighbourKey);
            }
        }
        return output;
    }

    private static AiRequest convert(int yourKey, GameState gameState) {
        Map<Integer, AiRequest.LandCell> landCells = new HashMap<>();
        GameState.Cell[] cells = gameState.getCells();
        for (int cellKey = 0; cellKey < cells.length; cellKey++) {
            GameState.Cell cell = cells[cellKey];

            Set<Integer> neighbours = neighbours(gameState.getNeighboursMatrix(), cellKey);
            landCells.put(cellKey, new AiRequest.LandCell(cell.getOccupierKey(), cell.getDefendingTroopSize(), neighbours));
        }

        AiRequest request = new AiRequest();
        request.setYourKey(yourKey);
        request.setReserveSize(gameState.getOpponents()[yourKey].getReserveSize());
        request.setLandCells(landCells);
        return request;
    }

    public static void main(String[] args) {
        //testDoughnutAttackRange_forFail();
        //testDoughnutAttackRange_forPass();
        //testDominionDemolish();

        testAi();
    }


//    public static void main(String[] args) {
//        GameState gameState = new GameState(5,4);
//        gameState.getCells()[cell1Key].setOccupierKey(player1Key);
//        gameState.getCells()[cell1Key].setDefendingTroopSize(55);
//
//        gameState.getCells()[cell2Key].setOccupierKey(player2Key);
//        gameState.getCells()[cell2Key].setDefendingTroopSize(40);
//
//        gameState.getCells()[cell3Key].setOccupierKey(player2Key);
//        gameState.getCells()[cell3Key].setDefendingTroopSize(10);
//        GameEngine engine = new GameEngine(gameState);
//
//
//
//        Set<GameEngine.Action> actionGroup = new HashSet<>();
//        actionGroup.add(new GameEngine.Action(player1Key,cell3Key,10));
//
//        GameState state = engine.doAction(actionGroup);
//
//
//        System.out.println(state);
//    }
}
