package hu.kalmancheysandor.application.dominion.api.game.common.tests;

import hu.kalmancheysandor.application.dominion.api.ai.basic.Basic1Engine;
import hu.kalmancheysandor.application.dominion.api.ai.basic.Basic2Engine;
import hu.kalmancheysandor.application.dominion.api.ai.common.AiEngine;
import hu.kalmancheysandor.application.dominion.api.ai.common.AiRequest;
import hu.kalmancheysandor.application.dominion.api.ai.common.AiResponse;
import hu.kalmancheysandor.application.dominion.api.game.common.engine.GameEngine;
import hu.kalmancheysandor.application.dominion.api.game.common.engine.GameState;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GameAdvancedTest {
    private final static int player0Key = 0;
    private final static int player1Key = 1;
    private final static int player2Key = 2;
    private final static int player3Key = 3;
    private final static int player4Key = 4;

    private final static int cell0Key = 0;
    private final static int cell1Key = 1;
    private final static int cell2Key = 2;
    private final static int cell3Key = 3;

    private static void println(String s) {
        System.out.println(s);
    }


    private static void testAi(int sizeX, int sizeY) {
        int cellTopLeft = 0;
        int cellTopRight = sizeX - 1;
        int cellBottomLeft = ((sizeX * sizeY) - sizeX);
        int cellBottomRight = (sizeX * sizeY) - 1;

        boolean[][] mrx = buildNeighbouringMatrix(sizeX, sizeY);
        GameState gameState = new GameState(3, sizeX * sizeY, mrx);

        gameState.getOpponents()[player0Key].setAlive(false);
        gameState.getOpponents()[player1Key].setAlive(true);
        gameState.getOpponents()[player2Key].setAlive(true);

        gameState.getOpponents()[player1Key].setReserveSize(100);
        gameState.getOpponents()[player2Key].setReserveSize(50);


        // Player1
        gameState.getCells()[cellTopLeft].setOccupierKey(player1Key);
        gameState.getCells()[cellTopLeft].setDefendingTroopSize(21);
        gameState.getCells()[cellBottomRight].setOccupierKey(player1Key);
        gameState.getCells()[cellBottomRight].setDefendingTroopSize(22);

        // Player2
        gameState.getCells()[cellTopRight].setOccupierKey(player2Key);
        gameState.getCells()[cellTopRight].setDefendingTroopSize(23);
        gameState.getCells()[cellBottomLeft].setOccupierKey(player2Key);
        gameState.getCells()[cellBottomLeft].setDefendingTroopSize(24);

        Set<GameEngine.Action> actionGroup;

        GameEngine engine = new GameEngine(gameState);
        AiEngine ai1 = new Basic1Engine();
        AiEngine ai2 = new Basic2Engine();

        GameState state = engine.getGameState();
        printStateMatrix(state.getCells(),sizeX,sizeY);

        int turn = 0;
        do {
            println("-----------<TURN:" + turn + ">----------");
            if (engine.isEndOfGame()) {
                println("END OF GAME");
                println("The winner is:"+engine.getGameState().getWinnerKey());
                break;
            }

            actionGroup = new HashSet<>();

            AiResponse response1 = ai1.generateResponse(convert(player1Key, state));
            AiResponse response2 = ai2.generateResponse(convert(player2Key, state));


            GameEngine.Action action1 = new GameEngine.Action(player1Key, response1.getTargetCellKey(), response1.getTroopSize());
            GameEngine.Action action2 = new GameEngine.Action(player2Key, response2.getTargetCellKey(), response2.getTroopSize());

            println("Player1 Action:" + action1);
            println("Player2 Action:" + action2);

            actionGroup.add(action1);
            actionGroup.add(action2);

            engine.doAction(actionGroup);
            state = engine.getGameState();
            printStateMatrix(state.getCells(),sizeX,sizeY);



            if (turn >= 100) {
                println("!!! <TO MUCH LOOP> !!!");
                break;
            }
            turn++;
        } while (true);


    }


    public static void main(String[] args) {
        //testDoughnutAttackRange_forFail();
        //testDoughnutAttackRange_forPass();
        //testDominionDemolish();

        testAi(5,5);
    }


    private static boolean[][] buildNeighbouringMatrix(int sizeX, int sizeY) {


        int count = sizeX * sizeY;
        boolean[][] neighbouringArr = new boolean[count][count];
        int maxIndex = count - 1;

        // Set default value
        for (int i = 0; i < count; i++) {
            for (int j = 0; j < count; j++) {
                neighbouringArr[i][j] = false;
            }
        }

        for (int cellIndex = 0; cellIndex < neighbouringArr.length; cellIndex++) {
            int leftNeighbourIndex = cellIndex - 1;
            int rightNeighbourIndex = cellIndex + 1;
            int topNeighbourIndex = cellIndex - sizeX;
            int bottomNeighbourIndex = cellIndex + sizeX;

            int window = (int)cellIndex/(int)sizeX;

            if (leftNeighbourIndex >= 0 && leftNeighbourIndex <= maxIndex && window==((int)leftNeighbourIndex/(int)sizeX)) {
                neighbouringArr[cellIndex][leftNeighbourIndex] = true;
            }

            if (rightNeighbourIndex >= 0 && rightNeighbourIndex <= maxIndex && window==((int)rightNeighbourIndex/(int)sizeX)) {
                neighbouringArr[cellIndex][rightNeighbourIndex] = true;
            }

            if (topNeighbourIndex >= 0 && topNeighbourIndex <= maxIndex) {
                neighbouringArr[cellIndex][topNeighbourIndex] = true;
            }

            if (bottomNeighbourIndex >= 0 && bottomNeighbourIndex <= maxIndex) {
                neighbouringArr[cellIndex][bottomNeighbourIndex] = true;
            }
        }
printNeighbouringMatrix(neighbouringArr);
        return neighbouringArr;
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

            Integer occupierKey = null;
            if (cell.getOccupierKey() > -1) {
                occupierKey = cell.getOccupierKey();
            }


            Set<Integer> neighbours = neighbours(gameState.getNeighboursMatrix(), cellKey);
            landCells.put(cellKey, new AiRequest.LandCell(occupierKey, cell.getDefendingTroopSize(), neighbours));
        }

        AiRequest request = new AiRequest();
        request.setYourKey(yourKey);
        request.setReserveSize(gameState.getOpponents()[yourKey].getReserveSize());
        request.setLandCells(landCells);
        return request;
    }

    private static void printNeighbouringMatrix(boolean[][] neighbouringMrx) {
        String x = "[][]";
        int count = neighbouringMrx.length;
        for (int i = 0; i < count; i++) {

            if (i < 10) {
                x += " 0" + i + "|";
            }
            else {
                x += " " + i + "|";
            }
        }
        System.out.println(x);
        for (int i = 0; i < count; i++) {
            String s="";
            if(i<10) {
                s="[0"+i+"] ";
            }
            else {
                s="["+i+"] ";
            }
            for (int j = 0; j < count; j++) {

                if(neighbouringMrx[i][j]){
                    s += "I | ";
                }
                else {
                    s += "_ | ";
                }
            }
            System.out.println(s);
        }
    }




    private static void printStateMatrix(GameState.Cell[] cells,int sizeX,int sizeY) {
        String x = "[][]";
        int count = cells.length;
//        for (int i = 0; i < sizeX; i++) {
//
//            if (i < 10) {
//                x += " 0" + i + "|";
//            }
//            else {
//                x += " " + i + "|";
//            }
//        }
//        System.out.println(x);
        for (int i = 0; i < sizeX; i++) {
            String s="";
//            if(i<10) {
//                s="[0"+i+"] ";
//            }
//            else {
//                s="["+i+"] ";
//            }
            for (int j = 0; j < sizeY; j++) {
            int index = (i*sizeX)+j;
                if(cells[index].isEmpty()){
                    s += " .|";
                }
                else {
                    s += cells[index].getOccupierKey()+"-"+cells[index].getDefendingTroopSize()+"|";
                }
            }
            System.out.println(s);
        }
    }

}
