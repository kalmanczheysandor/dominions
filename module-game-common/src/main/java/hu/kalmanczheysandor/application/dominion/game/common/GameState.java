package hu.kalmanczheysandor.application.dominion.game.common;


public class GameState {
    private StatusCode statusCode;
    private final int cellCount;
    private final int playerCount;

    private boolean[][] neighboursMatrix;
    private Cell[] cells;
    private Opponent[] opponents;

    public GameState(final int playerCount, final int cellCount) {
        this.playerCount = playerCount;
        this.cellCount = cellCount;
        this.neighboursMatrix = new boolean[cellCount][cellCount];
        this.statusCode = StatusCode.INITIALISED;

        this.opponents = new Opponent[playerCount];
        this.cells = new Cell[cellCount];
        init();
    }

    private void init() {
        for (int playerIndex = 0; playerIndex < this.playerCount; playerIndex++) {
            this.opponents[playerIndex] = new Opponent(10);
        }

        for (int cellIndex = 0; cellIndex < this.cellCount; cellIndex++) {
            this.cells[cellIndex] = new Cell();
        }
        this.neighboursMatrix[0][0] = false;
        this.neighboursMatrix[0][1] = true;
        this.neighboursMatrix[0][2] = true;
        this.neighboursMatrix[0][3] = false;

        this.neighboursMatrix[1][0] = true;
        this.neighboursMatrix[1][1] = false;
        this.neighboursMatrix[1][2] = false;
        this.neighboursMatrix[1][3] = true;

        this.neighboursMatrix[2][0] = true;
        this.neighboursMatrix[2][1] = false;
        this.neighboursMatrix[2][2] = false;
        this.neighboursMatrix[2][3] = true;

        this.neighboursMatrix[3][0] = false;
        this.neighboursMatrix[3][1] = true;
        this.neighboursMatrix[3][2] = true;
        this.neighboursMatrix[3][3] = false;
    }









    public static class Opponent {
        private int reserveSize = 0;
        private boolean alive =true;

        public Opponent(int reserveSize) {
            this.reserveSize = reserveSize;
        }

        public int getReserveSize() {
            return reserveSize;
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
            if(occupierKey==-1) {
                return true;
            }
            return false;
        }

        public void free() {
            occupierKey = -1;
            defendingTroopSize = 0;
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






    public StatusCode getStatusCode() {
        return statusCode;
    }

    public int getCellCount() {
        return cellCount;
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public boolean[][] getNeighboursMatrix() {
        return neighboursMatrix;
    }

    public Cell[] getCells() {
        return cells;
    }

    public Opponent[] getOpponents() {
        return opponents;
    }


    @Override
    public String toString() {
        String s ="";
        for(int i=0; i<cells.length;i++) {
            s += "(Cell:"+i+")[P:"+cells[i].getOccupierKey()+"| T:"+cells[i].getDefendingTroopSize()+"]\n";
        }

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
}
