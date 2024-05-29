package hu.kalmancheysandor.application.dominion.api.game.common.engine;


import hu.kalmancheysandor.application.dominion.api.game.common.engine.exception.GeneralGameException;
import hu.kalmancheysandor.application.dominion.api.game.common.engine.exception.UnexpectedCaseFoundGameException;

import java.util.*;

public class GameState {
    private StatusCode statusCode;
    private int cellCount;
    private int playerCount;
    private Integer winnerKey = null;

    private boolean[][] neighboursMatrix;
    private Cell[] cells;
    private Opponent[] opponents;


    public GameState(GameMap gameMap) {
        int cellCount = gameMap.getCells().size();
        int playerCount = gameMap.getPlayers().size();
        boolean[][] neighbouringMatrix = generateNeighbouringMatrix(gameMap);

        // Convert map cells to game state cells
        Cell[] cells = new Cell[cellCount];
        for (Map.Entry<Integer, GameMap.MapCell> mapCellEntry : gameMap.getCells().entrySet()) {
            int mapCellIndex = mapCellEntry.getKey();
            GameMap.MapCell mapCell = mapCellEntry.getValue();

            int defendingTroops = -1;
            if (mapCell.getPlayerKey() != null) {
                defendingTroops = mapCell.getPlayerKey();
            }
            cells[mapCellIndex] = new Cell(defendingTroops, mapCell.getArmySize());
        }

        // Convert map players to game state players
        Opponent[] players = new Opponent[playerCount];
        for (Map.Entry<Integer, GameMap.Opponent> mapPlayerEntry : gameMap.getPlayers().entrySet()) {
            int mapPlayerIndex = mapPlayerEntry.getKey();
            GameMap.Opponent mapPlayer = mapPlayerEntry.getValue();
            players[mapPlayerIndex] = new Opponent(mapPlayer.getReserveSize());
        }
        init(cells, players, neighbouringMatrix);

    }


    private void init(Cell[] cells, Opponent[] opponents, boolean[][] neighbouringMatrix) {
       int playerCount = opponents.length;
       int cellCount = cells.length;

        // Validations
        if (playerCount < 2) {
            throw new GeneralGameException("At leats two player needed!");
        }
        validateNeighbouringMatrix(cellCount, neighbouringMatrix);

        this.playerCount = playerCount;
        this.cellCount = cellCount;
        this.neighboursMatrix = neighbouringMatrix;
        this.statusCode = StatusCode.INITIALISED;

        this.opponents = opponents;
        this.cells = cells;
    }


    private GameState(int playerCount, int cellCount, boolean[][] neighbouringMatrix) {
        initialisation(playerCount, cellCount, neighbouringMatrix);
    }


    private void initialisation(int playerCount, int cellCount, boolean[][] neighbouringMatrix) {
        // Validations
        if (playerCount < 2) {
            throw new GeneralGameException("At leats two player needed!");
        }
        validateNeighbouringMatrix(cellCount, neighbouringMatrix);

        this.playerCount = playerCount;
        this.cellCount = cellCount;
        this.neighboursMatrix = neighbouringMatrix;
        this.statusCode = StatusCode.INITIALISED;

        this.opponents = new Opponent[playerCount];
        this.cells = new Cell[cellCount];
    }


    private static boolean[][] generateNeighbouringMatrix(GameMap gameMap) {
        int cellCount = gameMap.getCells().size();
        System.out.println("Cell count:" + cellCount);
        boolean[][] neighbouringMatrix = new boolean[cellCount][cellCount];

        for (Map.Entry<Integer, GameMap.MapCell> entry : gameMap.getCells().entrySet()) {
            GameMap.MapCell cell = entry.getValue();
            int cellIndex = entry.getKey();

            for (int neighbourIndex : entry.getValue().getNeighbours()) {
                neighbouringMatrix[cellIndex][neighbourIndex] = true;
            }
        }

        return neighbouringMatrix;
    }


    private static void validateNeighbouringMatrix(int expectedCellCount, boolean[][] neighbouringMatrix) {

        if (neighbouringMatrix.length != expectedCellCount) {
            throw new GeneralGameException("Neighbouring matrix outer size is not equal with expected cell count!");
        }

        for (int i = 0; i < expectedCellCount; i++) {
            if (neighbouringMatrix[i].length != expectedCellCount) {
                throw new GeneralGameException("Neighbouring matrix inner size at " + i + " index is not equal with expected cell count!");
            }
        }
    }


    public int getCellCount() {
        return cellCount;
    }

    public int getCellCountOfPlayer(int playerIndex) {
        int count = 0;
        GameState.Cell[] cells = getCells();

        for (int cellKey = 0; cellKey < cells.length; cellKey++) {

            if (cells[cellKey].getOccupierKey()==playerIndex) {
                count++;
            }
        }
        return count;
    }


    public int getPlayerCount() {
        return playerCount;
    }

    public int[] getAlivePlayers() {
        List<Integer> alivePlayers = new ArrayList<>();

        for(int playerIndex=0;playerIndex<opponents.length;playerIndex++) {
            GameState.Opponent player = opponents[playerIndex];
            if (player.isAlive()) {
                alivePlayers.add(playerIndex);
            }
        }
        return alivePlayers.stream().mapToInt(i -> i).toArray();
    }

    public boolean[][] getNeighboursMatrix() {
        return neighboursMatrix;
    }

    public Cell[] getCells() {
        return cells;
    }
    public Cell getCell(int cellKey) {
        return cells[cellKey];
    }
    public Opponent[] getOpponents() {
        return opponents;
    }
    public Opponent getOpponent(int playerKey) {
        return opponents[playerKey];
    }


    public Set<Integer> getEmptyCellKeys() {
        Set<Integer> emptyCellKeys = new HashSet<>();
        GameState.Cell[] cells = getCells();

        for (int cellKey = 0; cellKey < cells.length; cellKey++) {
            if (cells[cellKey].isEmpty()) {
                emptyCellKeys.add(cellKey);
            }
        }
        return emptyCellKeys;
    }

    public StatusCode getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(StatusCode statusCode) {
        this.statusCode = statusCode;
    }

    public Integer getWinnerKey() {
        return winnerKey;
    }

    public void setWinnerKey(Integer winnerKey) {
        this.winnerKey = winnerKey;
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
        s += "winnerKey:" + winnerKey + "\n";

        return s;
//
//
//
//
//
//
//        return "GameState{" +
//            "statusCode=" + statusCode +
//            ", cellCount=" + cellCount +
//            ", playerCount=" + playerCount +
//            ", neighboursMatrix=" + Arrays.toString(neighboursMatrix) +
//            ", cells=" + Arrays.toString(cells) +
//            ", opponents=" + Arrays.toString(opponents) +
//            '}';
    }


    public static class Opponent {
        private int reserveSize = 0;
        private boolean alive = true;

        public Opponent(int reserveSize) {
            this.reserveSize = reserveSize;
        }

        public int getReserveSize() {
            return reserveSize;
        }

        public void setReserveSize(int reserveSize) {
            if (reserveSize < 0) {
                throw new UnexpectedCaseFoundGameException("Reserve size must not be less than 0!");
            }
            this.reserveSize = reserveSize;
        }

        public boolean isAlive() {
            return alive;
        }

        public void setAlive(boolean alive) {
            this.alive = alive;
        }

        public void incrementReserveSize(int incrementWithValue) {
            this.reserveSize += incrementWithValue;
        }
    }

    public static class Cell {
        private int occupierKey = -1;
        private int defendingTroopSize = 0;

        public Cell() {
        }

        public Cell(int occupierKey, int defendingTroopSize) {
            this.occupierKey = occupierKey;
            this.defendingTroopSize = defendingTroopSize;
        }

        public int getOccupierKey() {
            return occupierKey;
        }

        public int getDefendingTroopSize() {
            return defendingTroopSize;
        }

        public void setOccupierKey(int occupierKey) {
            this.occupierKey = occupierKey;
        }

        public void setDefendingTroopSize(int defendingTroopSize) {
            this.defendingTroopSize = defendingTroopSize;
        }

        public boolean isEmpty() {
            if (occupierKey == -1) {
                return true;
            }
            return false;
        }

        public void free() {
            occupierKey = -1;
            defendingTroopSize = 0;
        }

        @Override
        public String toString() {
            return "Cell{" + "occupierKey=" + occupierKey + ", defendingTroopSize=" + defendingTroopSize + '}';
        }
//        public void decrementTroopSize(int decrementWithValue) {
//            this.troopSize -= decrementWithValue;
//            if(this.troopSize<0) {
//                this.troopSize=0;
//            }
//
//        }
    }

    public enum StatusCode {
        INITIALISED,
        PROCEEDED,
        FINISHED
    }


}
