package hu.kalmanczheysandor.application.dominion.game.common;

import java.util.HashSet;
import java.util.Set;

public class GameTest {
    private final static int player1Key=0;
    private final static int player2Key=1;

    private final static int cell1Key=0;
    private final static int cell2Key=1;
    private final static int cell3Key=2;
    private final static int cell4Key=3;

    public static void main(String[] args) {
        GameState gameState = new GameState(2,4);
        GameEngine engine = new GameEngine(gameState);

        Set<GameEngine.Action> actionGroup = new HashSet<>();
        actionGroup.add(new GameEngine.Action(player1Key,cell1Key,1));
        actionGroup.add(new GameEngine.Action(player2Key,cell2Key,1));

        GameState state = engine.doAction(actionGroup);






        System.out.println(state);
    }
}
