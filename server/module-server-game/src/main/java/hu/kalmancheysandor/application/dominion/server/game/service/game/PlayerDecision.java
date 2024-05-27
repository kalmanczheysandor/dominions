package hu.kalmancheysandor.application.dominion.server.game.service.game;

import hu.kalmancheysandor.application.dominion.api.game.common.engine.GameState;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
public class PlayerDecision {
    private int reserveSize = 0;
    private int enemiesCount = 0;
    private Map<Integer, Field> fields = new HashMap<Integer, Field>();
    private Move decision;

    @Data
    @AllArgsConstructor
    public static class Field {
        public Integer playerKey;
        public int defenders;
    }

    @Data
    @AllArgsConstructor
    public static class Move {
        private int target;
        private int troops;
    }


    public static PlayerDecision create(Integer target, int troopSize, int reserveSize, int enemiesCount, GameState gameState) {
        // Fields
        Map<Integer, Field> fields = new HashMap<>();
        for (int cellIndex = 0; cellIndex < gameState.getCells().length; cellIndex++) {
            GameState.Cell cell = gameState.getCells()[cellIndex];
            fields.put(cellIndex, new Field(cell.getOccupierKey(), cell.getDefendingTroopSize()));
        }

        //Move
        Move move = new Move(target, troopSize);

        return new PlayerDecision(reserveSize, enemiesCount, fields, move);
    }
}
