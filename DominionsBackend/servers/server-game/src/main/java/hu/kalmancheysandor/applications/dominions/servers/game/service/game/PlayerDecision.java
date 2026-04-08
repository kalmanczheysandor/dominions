package hu.kalmancheysandor.applications.dominions.servers.game.service.game;


import hu.kalmancheysandor.applications.dominions.apis.game.common.representation.state.GameState;
import hu.kalmancheysandor.applications.dominions.apis.game.common.representation.state.GameStateCell;
import hu.kalmancheysandor.applications.dominions.apis.game.common.representation.state.GameStatePlayer;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.*;

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
        public Integer rankIndex;
        public int defenders;
    }

    @Data
    @AllArgsConstructor
    public static class Move {
        private int target;
        private int troops;
    }


    public static PlayerDecision create(Integer supportedPlayerIndex, Integer target, int troopSize, int reserveSize, int enemiesCount, GameState gameState) {
        Map<Integer, Integer> rankMap = createRankMapOfPlayers(supportedPlayerIndex, gameState);

        // Fields
        Map<Integer, Field> fields = new HashMap<>();
        for (int cellIndex = 0; cellIndex < gameState.getCells().length; cellIndex++) {
            GameStateCell cell = gameState.getCells()[cellIndex];
            int occupierKey = cell.getOccupierKey();        // -1 = empty, 0= first player index, ... other player indexes
            int playerKey = occupierKey+1;  // 0 = empty, 1= first player index, ... other player indexes

            fields.put(cellIndex, new Field(playerKey, cell.getDefendingTroopSize()));
        }
        if (target == null) {
            target = -1;
        }
        //Move
        Move move = new Move(target, troopSize);

        return new PlayerDecision(reserveSize, enemiesCount, fields, move);
    }

    private static Map<Integer, Integer> createRankMapOfPlayers(Integer supportedPlayerIndex, GameState gameState) {
        // Building list of playerIndex - power pairs
        LinkedHashMap<Integer, Integer> inputMap = new LinkedHashMap<>();
        int playerIndex = 0;
        for (GameStatePlayer player : gameState.getOpponents()) {
            if (player.isAlive()) {
                int power = gameState.getOccupiedCellsCountOfPlayer(playerIndex);
                inputMap.put(playerIndex, power);
            }
            playerIndex++;
        }

        // Keep Original key orders
        List<Integer> keys = new ArrayList<>(inputMap.keySet());

        // Arrange player keys by descending order of power of players
        List<Integer> sortedPlayerKeys = new ArrayList<>(keys);
        sortedPlayerKeys.sort((k1, k2) -> {
            int cmp = inputMap.get(k2).compareTo(inputMap.get(k1));
            if (cmp != 0) {
                return cmp;
            } else {        // When both player equally strong then their player index is observed
                return Integer.compare(keys.indexOf(k1), keys.indexOf(k2)); // Smaller index is to first
            }
        });

        // Descending order of players (except supported player)
        Map<Integer, Integer> rankMap = new HashMap<>();
        rankMap.put(1, supportedPlayerIndex);            // Supported player must be first anyway
        for (Integer playerKey : sortedPlayerKeys) {
            if (!rankMap.containsKey(playerKey)) {
                rankMap.put(playerKey,rankMap.size() + 1);
            }
        }

        return rankMap;
    }

}
