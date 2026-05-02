package hu.kalmancheysandor.applications.dominions.apis.ai.general.common;


import com.fasterxml.jackson.databind.ObjectMapper;
import hu.kalmancheysandor.applications.dominions.apis.game.common.representation.state.GameState;
import hu.kalmancheysandor.applications.dominions.apis.game.common.representation.state.GameStateCell;
import hu.kalmancheysandor.applications.dominions.apis.game.common.representation.state.GameStatePlayer;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class TAiEngine implements IAiEngine {


    public final static int ENEMIES_COUNT_MIN = 0;
    public final static int ENEMIES_COUNT_MAX = 10;

    public final static int OWNER_RANKS_INDEX_MIN = 0;
    public final static int OWNER_RANKS_INDEX_MAX = 10;

    public final static int DEFENDERS_COUNT_MIN = 0;
    public final static int DEFENDERS_COUNT_MAX = 100;

    public final static int RESERVE_SIZE_MIN = 0;
    public final static int RESERVE_SIZE_MAX = 100;


    public final static int TROOPS_SIZE_MIN = 0;
    public final static int TROOPS_SIZE_MAX = 100;

    public final static int CELL_C0UNT = 42;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /// Game state methods /////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    protected static int countPlayerCells(GameState gameState, int playerKey) {
        int count = 0;
        for (GameStateCell cell : gameState.getCells()) {
            if (cell.getOccupierKey() == playerKey) {
                count++;
            }
        }
        return count;
    }

    protected static Set<Integer> collectCellKeysOfAPlayer(GameState gameState, int playerKey) {
        Set<Integer> playerCellKeys =  new HashSet<>();
        int key = 0;
        for (GameStateCell cell : gameState.getCells()) {
            if (cell.getOccupierKey() == playerKey) {
                playerCellKeys.add(key);
            }
            key++;
        }
        return playerCellKeys;
    }

    protected static Map<Integer,GameStateCell> collectPlayerCells(GameState gameState, int playerKey) {
        Map<Integer,GameStateCell> playerCells =  new HashMap<>();
        int key = 0;
        for (GameStateCell cell : gameState.getCells()) {
            if (cell.getOccupierKey() == playerKey) {
                playerCells.put(key,cell);
            }
            key++;
        }
        return playerCells;
    }

    protected static Set<Integer> neighbourCellKeysOfACell(GameState gameState,int cellKey){
        Map<Integer,GameStateCell> neighbourCells = collectNeighbourCellsOfACell(gameState,cellKey);
        Set<Integer> neighbourKeys = new HashSet<>();
        for(Map.Entry<Integer,GameStateCell> entry: neighbourCells.entrySet()) {
            neighbourKeys.add(entry.getKey());
        }
        return neighbourKeys;
    }

    protected static Map<Integer,GameStateCell> collectNeighbourCellsOfACell(GameState state, int cellKey) {
        Map<Integer,GameStateCell> realNeighbours = new HashMap<>();

        boolean[] neighboursRow = state.getNeighboursMatrix()[cellKey];
        for (int neighbourKey = 0; neighbourKey < neighboursRow.length; neighbourKey++) {
            if (neighboursRow[neighbourKey] == true) {
                realNeighbours.put(neighbourKey,state.getCell(neighbourKey));
            }
        }
        return realNeighbours;
    }

    protected static Set<Integer> collectAllAlivePlayerKeys(GameState gameState) {
        Set<Integer> alivePlayerKeys = new HashSet<>();

        int playerKey = 0;
        for (GameStatePlayer opponent : gameState.getOpponents()) {
            if (opponent.isAlive()) {
                alivePlayerKeys.add(playerKey);
            }
            playerKey++;
        }
        return alivePlayerKeys;
    }
    protected static int contOfAlivePlayers(GameState gameState){
        int count =0;
        for (GameStatePlayer opponent : gameState.getOpponents()) {
            if (opponent.isAlive()) {
                count++;
            }
        }
        return count;
    }

    protected static Set<Integer> collectCellKeysOfCurrentAttackZoneOfPlayer(GameState gameState, int playerKey) {

        Set<Integer> cellKeys = collectCellKeysOfAPlayer(gameState, playerKey);

        Map<Integer,GameStateCell> neighbourCells;
        Integer neighbourCellKey;
        GameStateCell neighbourCell;
        Set<Integer> attackZoneKeys = new HashSet<>();
        for(Integer cellKey:cellKeys) {
            neighbourCells = collectNeighbourCellsOfACell(gameState,cellKey);

            for(Map.Entry<Integer,GameStateCell> entry:neighbourCells.entrySet()) {
                neighbourCellKey = entry.getKey();
                neighbourCell = entry.getValue();
                if(neighbourCell.isEmpty() || neighbourCell.getOccupierKey()!=playerKey) {
                    attackZoneKeys.add(neighbourCellKey);
                }
            }
        }

        return  attackZoneKeys;
    }

    protected static boolean isCellInAttackZoneOfPlayer(GameState gameState, int playerKey,int observedCellKey) {
        Set<Integer> cellKeysOfAttackZone = collectCellKeysOfCurrentAttackZoneOfPlayer(gameState,playerKey);
        return cellKeysOfAttackZone.contains(observedCellKey);
    }

    protected static int getPlayerReserveSize(GameState gameState, int playerKey) {
        return gameState.getOpponent(playerKey).getReserveSize();
    }

    protected static int calculatePlayerMaxAttackPower(GameState gameState,int playerKey) {
        int reserveSize = gameState.getOpponent(playerKey).getReserveSize();
        return reserveSize>TROOPS_SIZE_MAX?TROOPS_SIZE_MAX:reserveSize;
    }

    protected static GameState createJsonContentToGameState(String jsonContent) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(jsonContent, GameState.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    protected static String createGameStateToJsonContent(GameState gameState) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(gameState);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
