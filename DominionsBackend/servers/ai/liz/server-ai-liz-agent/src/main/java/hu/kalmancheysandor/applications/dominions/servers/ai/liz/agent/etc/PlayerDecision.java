package hu.kalmancheysandor.applications.dominions.servers.ai.liz.agent.etc;


import hu.kalmancheysandor.applications.dominions.apis.ai.engine.liz.LizAiEngine;
import hu.kalmancheysandor.applications.dominions.apis.game.common.representation.state.GameState;
import hu.kalmancheysandor.applications.dominions.apis.game.common.representation.state.GameStateCell;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerDecision {
    private int reserveSize = 0;
    private int enemiesCount = 0;
    private Map<Integer, Field> fields = new HashMap<Integer, Field>();
    private Move decision;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Field {
        public int rankIndex;
        public int defenders;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Move {
        private int target;
        private int troops;
    }


    public static PlayerDecision create(Integer supportedPlayerIndex, Integer target, int troopSize, int reserveSize, int enemiesCount, GameState gameState) {
        Map<Integer, Integer> rankMap = LizAiEngine.createRankMapOfOccupierIndexes(gameState,supportedPlayerIndex );

        // Fields
        Map<Integer, Field> fields = new HashMap<>();
        for(int cellIndex = 0; cellIndex < gameState.getCells().length; cellIndex++) {
            GameStateCell cell = gameState.getCells()[cellIndex];
            int occupierKey = cell.getOccupierKey();        // -1 = empty, 0= first player index, ... other player indexes

            // Find the rank-index related to the player-index
            if(!rankMap.containsKey(occupierKey)) {
                throw new RuntimeException("Occupier Key("+occupierKey+") not found in rank-map");
            }
            int rankIndex   = rankMap.get(occupierKey);

            //
            fields.put(cellIndex, new Field(rankIndex, cell.getDefendingTroopSize()));
        }

        if (target == null) {
            target = -1;
        }

        //Move
        Move move = new Move(target, troopSize);

        return new PlayerDecision(reserveSize, enemiesCount, fields, move);
    }
}
