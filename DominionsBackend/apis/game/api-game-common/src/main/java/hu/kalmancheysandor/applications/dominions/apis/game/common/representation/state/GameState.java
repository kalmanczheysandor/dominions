package hu.kalmancheysandor.applications.dominions.apis.game.common.representation.state;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.kalmancheysandor.applications.dominions.apis.game.common.map.configuration.GameMapConfiguration;
import hu.kalmancheysandor.applications.dominions.apis.game.common.map.configuration.GameMapConfigurationBoardCell;
import hu.kalmancheysandor.applications.dominions.apis.game.common.map.state.GameMapStateCell;
import hu.kalmancheysandor.applications.dominions.apis.game.common.map.state.GameMapStatePlayer;
import hu.kalmancheysandor.applications.dominions.apis.game.common.representation.state.exception.GeneralGameStateException;
import hu.kalmancheysandor.applications.dominions.apis.game.common.map.GameMap;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.*;

@Data
@NoArgsConstructor
@JsonAutoDetect(
    fieldVisibility = JsonAutoDetect.Visibility.ANY,
    getterVisibility = JsonAutoDetect.Visibility.NONE,
    isGetterVisibility = JsonAutoDetect.Visibility.NONE,
    setterVisibility = JsonAutoDetect.Visibility.NONE
)
public class GameState {
    private GameStateStatusCode statusCode;
    private int cellCount;
    private int playerCount;
    //private Integer winnerKey = null;
    private int[] winnerKeys;
    private boolean[][] neighboursMatrix;
    private GameStateCell[] cells;
    private GameStatePlayer[] opponents;


    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///  FACTORY METHODS /////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static GameState deepCopy(GameState gameState) {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonContent;
        try {
            jsonContent = objectMapper.writeValueAsString(gameState);
            return objectMapper.readValue(jsonContent, GameState.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static GameState createByGameMapState(GameMap gameMap) {
        int cellCount = gameMap.getConfiguration().getCellCount();
        int playerCount = gameMap.getConfiguration().getPlayerCount();
        boolean[][] neighbouringMatrix = generateNeighbouringMatrix(gameMap.getConfiguration());

        // Convert map cells to game state cells
        GameStateCell[] cells = new GameStateCell[cellCount];
        for (Map.Entry<Integer, GameMapStateCell> mapCellEntry : gameMap.getInitialState().getCells().entrySet()) {
            int mapCellIndex = mapCellEntry.getKey();
            GameMapStateCell mapCell = mapCellEntry.getValue();

            int defendingTroops = -1;
            if (mapCell.getOccupierKey() != null) {
                defendingTroops = mapCell.getOccupierKey();
            }
            cells[mapCellIndex] = new GameStateCell(defendingTroops, mapCell.getArmySize());
        }

        // Convert map players to game state players
        GameStatePlayer[] players = new GameStatePlayer[playerCount];
        for (Map.Entry<Integer, GameMapStatePlayer> mapPlayerEntry : gameMap.getInitialState().getPlayers().entrySet()) {
            int mapPlayerIndex = mapPlayerEntry.getKey();
            GameMapStatePlayer mapPlayer = mapPlayerEntry.getValue();
            players[mapPlayerIndex] = new GameStatePlayer(mapPlayer.getReserveSize());
        }
        return new GameState(cells, players, neighbouringMatrix);
    }

    private static boolean[][] generateNeighbouringMatrix(GameMapConfiguration gameMapConfiguration) {
        int cellCount = gameMapConfiguration.getCellCount();
        boolean[][] neighbouringMatrix = new boolean[cellCount][cellCount];

        for (Map.Entry<Integer, GameMapConfigurationBoardCell> entry : gameMapConfiguration.getBoardCells().entrySet()) {
            GameMapConfigurationBoardCell cell = entry.getValue();
            int cellIndex = entry.getKey();

            for (int neighbourIndex : entry.getValue().getNeighbours()) {
                neighbouringMatrix[cellIndex][neighbourIndex] = true;
            }
        }

        return neighbouringMatrix;
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    /// INITIALISATION METHODS ///////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////

    public GameState(GameStateCell[] cells, GameStatePlayer[] opponents, boolean[][] neighbouringMatrix) {
        init(cells, opponents, neighbouringMatrix);
    }

    private GameState(int playerCount, int cellCount, boolean[][] neighbouringMatrix) {
        initialisation(playerCount, cellCount, neighbouringMatrix);
    }

    private void init(GameStateCell[] cells, GameStatePlayer[] opponents, boolean[][] neighbouringMatrix) {
        int playerCount = opponents.length;
        int cellCount = cells.length;

        // Validations
        if (playerCount < 2) {
            throw new GeneralGameStateException("At least two player needed!");
        }
        validateNeighbouringMatrix(cellCount, neighbouringMatrix);

        this.playerCount = playerCount;
        this.cellCount = cellCount;
        this.neighboursMatrix = neighbouringMatrix;
        this.statusCode = GameStateStatusCode.INITIALISED;

        this.opponents = opponents;
        this.cells = cells;
    }

    private void initialisation(int playerCount, int cellCount, boolean[][] neighbouringMatrix) {
        // Validations
        if (playerCount < 2) {
            throw new GeneralGameStateException("At leats two player needed!");
        }
        validateNeighbouringMatrix(cellCount, neighbouringMatrix);

        this.playerCount = playerCount;
        this.cellCount = cellCount;
        this.neighboursMatrix = neighbouringMatrix;
        this.statusCode = GameStateStatusCode.INITIALISED;

        this.opponents = new GameStatePlayer[playerCount];
        this.cells = new GameStateCell[cellCount];
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    /// VALIDATION METHODS ///////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static void validate(GameState gameState) {
        if(gameState.getStatusCode()==GameStateStatusCode.FINISHED) {
            if(!gameState.isNoMoreEmptyCell()) {
                throw new GeneralGameStateException("Game state is not a final state because there is at least one empty cell!");
            }
        }
    }


    private static void validateNeighbouringMatrix(int expectedCellCount, boolean[][] neighbouringMatrix) {

        if (neighbouringMatrix.length != expectedCellCount) {
            throw new GeneralGameStateException("Neighbouring matrix outer size is not equal with expected cell count!");
        }

        for (int i = 0; i < expectedCellCount; i++) {
            if (neighbouringMatrix[i].length != expectedCellCount) {
                throw new GeneralGameStateException("Neighbouring matrix inner size at " + i + " index is not equal with expected cell count!");
            }
        }
    }

    private boolean isNoMoreEmptyCell() {
        for (GameStateCell cell : getCells()) {
            if (cell.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public boolean isFinalState() {
//        if (isNoMoreEmptyCell() && countAlivePlayers() <= 2) {
        if (isNoMoreEmptyCell()) {
            return true;
        }
        return false;
    }

    private int countAlivePlayers() {
        return findAlivePlayers().size();
    }

    private List<GameStatePlayer> findAlivePlayers() {
        List<GameStatePlayer> alivePlayers = new ArrayList<>();

        for (GameStatePlayer opponent : getOpponents()) {
            if (opponent.isAlive()) {
                alivePlayers.add(opponent);
            }
        }
        return alivePlayers;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    /// GETTER & SETTER METHODS //////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////

    public int getCellCount() {
        return this.cells != null ? this.cells.length : 0;
    }

    public int getOccupiedCellsCountOfPlayer(int playerIndex) {
        int count = 0;
        GameStateCell[] cells = getCells();

        for (int cellKey = 0; cellKey < cells.length; cellKey++) {

            if (cells[cellKey].getOccupierKey() == playerIndex) {
                count++;
            }
        }
        return count;
    }

    public int getEmptyCellCount() {
        return this.getEmptyCellKeys().size();
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public int[] getAlivePlayers() {
        List<Integer> alivePlayers = new ArrayList<>();

        for (int playerIndex = 0; playerIndex < opponents.length; playerIndex++) {
            GameStatePlayer player = opponents[playerIndex];
            if (player.isAlive()) {
                alivePlayers.add(playerIndex);
            }
        }
        return alivePlayers.stream().mapToInt(i -> i).toArray();
    }

    public boolean[][] getNeighboursMatrix() {
        return neighboursMatrix;
    }

    public GameStateCell[] getCells() {
        return cells;
    }

    public GameStateCell getCell(int cellKey) {
        return cells[cellKey];
    }

    public GameStatePlayer[] getOpponents() {
        return opponents;
    }

    public GameStatePlayer getOpponent(int playerKey) {
        return opponents[playerKey];
    }

    public Set<Integer> getEmptyCellKeys() {
        Set<Integer> emptyCellKeys = new HashSet<>();
        GameStateCell[] cells = getCells();

        for (int cellKey = 0; cellKey < cells.length; cellKey++) {
            if (cells[cellKey].isEmpty()) {
                emptyCellKeys.add(cellKey);
            }
        }
        return emptyCellKeys;
    }

    public GameStateStatusCode getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(GameStateStatusCode gameStateStatusCode) {
        this.statusCode = gameStateStatusCode;
    }

    public int[] getWinnerKeys() {
        return winnerKeys;
    }

    public void setWinnerKeys(int[] winnerKeys) {
        this.winnerKeys = winnerKeys;
    }

    @Override
    public String toString() {
        String s = "";
        for (int i = 0; i < cells.length; i++) {
            String occupierSymbol = String.valueOf(cells[i].getOccupierKey());
            if (cells[i].getOccupierKey() == -1) {
                occupierSymbol = " ";
            }


            s += "(Cell:" + (i) + ")[P:" + occupierSymbol + "| T:" + cells[i].getDefendingTroopSize() + "]\n";
        }
        s += "-------------------------\n";
        s += "status:" + statusCode + "\n";
//        s += "winnerKey:" + winnerKey + "\n";

        return s;
    }


}
