package hu.kalmanczheysandor.application.dominion.game.common;

import java.util.HashSet;
import java.util.Set;

public class GameTest {
    private final static int player0Key=0;
    private final static int player1Key=1;
    private final static int player2Key=2;
    private final static int player3Key=3;
    private final static int player4Key=4;

    private final static int cell1Key=0;
    private final static int cell2Key=1;
    private final static int cell3Key=2;
    private final static int cell4Key=3;


    private static void testDoughnutAttackRange_forFail() {
        GameState gameState = new GameState(5,4);

        gameState.getCells()[cell2Key].setOccupierKey(player1Key);
        gameState.getCells()[cell2Key].setDefendingTroopSize(40);

        gameState.getCells()[cell3Key].setOccupierKey(player1Key);
        gameState.getCells()[cell3Key].setDefendingTroopSize(10);

        gameState.getCells()[cell4Key].setOccupierKey(player3Key);
        gameState.getCells()[cell4Key].setDefendingTroopSize(1);


        GameEngine engine = new GameEngine(gameState);



        Set<GameEngine.Action> actionGroup = new HashSet<>();
        actionGroup.add(new GameEngine.Action(player1Key,cell4Key,10));

        GameState state = engine.doAction(actionGroup);


        System.out.println(state);
    }

    private static void testDoughnutAttackRange_forPass() {
        GameState gameState = new GameState(5,4);

        gameState.getCells()[cell2Key].setOccupierKey(player1Key);
        gameState.getCells()[cell2Key].setDefendingTroopSize(40);

        gameState.getCells()[cell3Key].setOccupierKey(player1Key);
        gameState.getCells()[cell3Key].setDefendingTroopSize(10);


        GameState state;
        Set<GameEngine.Action> actionGroup;
        GameEngine engine = new GameEngine(gameState);

        actionGroup = new HashSet<>();
        actionGroup.add(new GameEngine.Action(player1Key,cell1Key,1));
        state = engine.doAction(actionGroup);
        System.out.println(state);


        actionGroup = new HashSet<>();
        actionGroup.add(new GameEngine.Action(player1Key,cell4Key,2));
        state = engine.doAction(actionGroup);

        System.out.println(state);
    }

    private static void testDominionDemolish() {
        GameState gameState = new GameState(5,4);

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
    }

    public static void main(String[] args) {
        //testDoughnutAttackRange_forFail();
        //testDoughnutAttackRange_forPass();
        testDominionDemolish();
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
