package hu.kalmanczheysandor.application.dominion.game.common;

import hu.kalmanczheysandor.application.dominion.game.common.exception.*;

import java.util.*;

public class GameEngine {
    private GameState gameState;

    public GameEngine(GameState gameState) {
        this.gameState = gameState;
    }

    public GameState doAction(Set<Action> plannedActions) {
        // Validations
        validatePlayersAction(plannedActions);

        // Grouping and resolving conflicting actions
        Map<Integer, Set<Action>> group = new HashMap<>();
        for (Action observedAction : plannedActions) {

            if (!group.containsKey(observedAction.getTargetCellKey())) {// The first action in the group
                Set<Action> actions = new HashSet<>();
                actions.add(observedAction);
                group.put(observedAction.getTargetCellKey(), actions);
            }
            else { // After the first action in the group
                Set<Action> actions = group.get(observedAction.getTargetCellKey());
                actions.add(observedAction);
            }
        }
        //  System.out.println(group);

        // find the highest offer
        Set<Action> actionsToProcess = new HashSet<>();
        for (Set<Action> actions : group.values()) {
            Action a = chooseTheHighestOffer(actions);
            if (a != null) {
                actionsToProcess.add(a);
            }
        }

        // Calculate the outcome of battles
        for (Action processedAction : actionsToProcess) {
            GameState.Cell cell = getCell(processedAction.getTargetCellKey());

            if (cell.getDefendingTroopSize() < processedAction.getAttackingTroopSize()) {
                cell.setDefendingTroopSize(processedAction.getAttackingTroopSize() - cell.getDefendingTroopSize());
                cell.setOccupierKey(processedAction.getPlayerKey());
            }
            else if (cell.getDefendingTroopSize() > processedAction.getAttackingTroopSize()) {
                cell.setDefendingTroopSize(cell.getDefendingTroopSize() - processedAction.getAttackingTroopSize());
            }
            else {
                cell.setDefendingTroopSize(0);
                cell.setOccupierKey(-1);
            }
        }

        // Removal of the weakest player in case of no more empty cell
        if (isNoMoreEmptyCell() && countAlivePlayers() > 2) {
//            System.out.println("isNoMoreEmptyCell");

            Integer weakestPlayerKey = findTheWeakestPlayer();
//            System.out.println("weakestPlayerKey:"+weakestPlayerKey);

            if (weakestPlayerKey != null) {
//                System.out.println("DEMOLISH:"+weakestPlayerKey);
                demolishPlayerDominion(weakestPlayerKey);
                getPlayer(weakestPlayerKey).setAlive(false);
            }
        }


        incrementAllReserve();
        return gameState;
    }

    private void validatePlayersAction(Set<Action> actions) {
        GameState.Cell targetCell;
        GameState.Opponent player;
        int playerKey;
        int targetCellKey;

        for (Action action : actions) {
            playerKey = action.getPlayerKey();
            targetCellKey = action.getTargetCellKey();

            targetCell = getCell(targetCellKey);
            player = getPlayer(playerKey);

            // When it is an attack without troops
            if (action.getAttackingTroopSize() == 0) {
                throw new NoTroopsWereSentActionException(playerKey);
            }

            // When it is an attack but the army size is overcalculated
            if (player.getReserveSize() < action.getAttackingTroopSize()) {
                throw new NotEnoughSupplyActionException(playerKey, action.getAttackingTroopSize(), player.getReserveSize());
            }

            // When the cell attacked belongs to the attacker and neither to the enemy and nor empty.
            if (targetCell.getOccupierKey() == action.getPlayerKey()) {
//                System.out.println("SElf attack: cellKey:" + targetCellKey + " " + targetCell.getOccupierKey() + " - " + action.getPlayerKey());
                throw new SelfAttackActionException(playerKey, targetCellKey);
            }

            if (!isCellANeighbourOfPlayer(action.getPlayerKey(), action.getTargetCellKey())) {
                throw new OutOfAttackRangeActionException(action.getPlayerKey(), action.getTargetCellKey());
            }


            if (isPlayerCausingDoughnutEffect(action.getPlayerKey())) {
//                System.out.println("DOUGHNUT ? ["+action.getPlayerKey()+"] >> yes ");
                if (!isCellAnEmptyNeighbourOfPlayer(action.getPlayerKey(), action.getTargetCellKey())) {
                    throw new OutOfDoughnutAttackRangeActionException(action.getPlayerKey(), action.getTargetCellKey());
                }
            }
            else {
//                System.out.println("DOUGHNUT ? ["+action.getPlayerKey()+"] >> no ");
            }

        }
    }

    private Action chooseTheHighestOffer(Set<Action> actions) {
        int highestValue = 0;
        int count = 0;
        Action a = null;
        for (Action action : actions) {
            if (action.getAttackingTroopSize() == highestValue) {
                count++;
                a = null;
            }
            else if (action.getAttackingTroopSize() > highestValue) {
                highestValue = action.getAttackingTroopSize();
                count = 1;
                a = action;
            }
        }

        if (count == 1 && a != null) {
            return a;
        }
        return null;
    }

    private void incrementAllReserve() {
        for (GameState.Opponent player : gameState.getOpponents()) {
            player.incrementReserveSize(1);
        }
    }

    private void demolishPlayerDominion(int playerKey) {
        for (GameState.Cell cell : gameState.getCells()) {
            if (cell.getOccupierKey() == playerKey) {
                cell.free();
            }
        }


    }


    private Integer findTheWeakestPlayer() {
        //System.out.println("findTheWeakestPlayer");
        int weakestValue = Integer.MAX_VALUE;
        int foundCount = 0;
        Integer weakestKey = null;

        GameState.Opponent[] opponents = gameState.getOpponents();
        for (int playerKey = 0; playerKey < opponents.length; playerKey++) {
            GameState.Opponent player = opponents[playerKey];

            if(player.isAlive()) {
                //System.out.println("--------" + playerKey+"--------");
                int dominionSize = countPlayerCells(playerKey);

//                System.out.println("PlayerIndex:" + playerKey);
//                System.out.println("dominionSize:" + dominionSize);


                if (dominionSize == weakestValue) {
                    foundCount++;
                    weakestKey = null;
                }
                else if (dominionSize < weakestValue) {
                    weakestValue = dominionSize;
                    foundCount = 1;
                    weakestKey = playerKey;
                }
            }
        }

        if (foundCount == 1 && weakestKey != null) {
            return weakestKey;
        }
        return null;
    }

    private boolean isCellANeighbourOfPlayer(int playerKey, int cellKey) {
        GameState.Cell observedCell = getCell(cellKey);

        // Any cell occupied by player is not a counted as a neighbour of that player
        if (observedCell.getOccupierKey() == playerKey) {
            return false;
        }

        for (GameState.Cell neighbourCell : getNeighboursOfCell(cellKey)) {
            if (neighbourCell.getOccupierKey() == playerKey) { // When the neighbour cell is occupied by the player
                return true;
            }
        }
        return false;
    }

    private boolean isCellAnEmptyNeighbourOfPlayer(int playerKey, int cellKey) {
        GameState.Cell observedCell = getCell(cellKey);

        // Any cell occupied by the player is not a counted as a neighbour of that player
        if (observedCell.getOccupierKey() == playerKey) {
            return false;
        }

        // Cell must be empty
        if (!observedCell.isEmpty()) {
            return false;
        }


        for (GameState.Cell neighbourCell : getNeighboursOfCell(cellKey)) {
            if (neighbourCell.getOccupierKey() == playerKey) { // When the neighbour cell is occupied by the player
                return true;
            }
        }
        return false;
    }

    private boolean isPlayerCausingDoughnutEffect(int playerKey) {
        for (int emptyCellKey : getEmptyCellKeys()) {
            if (isCellBlockedByPlayer(emptyCellKey, playerKey)) {
                return true;
            }
        }
        return false;
    }

    private boolean isCellBlockedByPlayer(int cellKey, int playerKey) {
//        System.out.println("isCellBlockedByPlayer(C:"+cellKey+",P:"+playerKey+")");


        for (GameState.Cell neighbourCell : getNeighboursOfCell(cellKey)) {
//            System.out.println("-neighbourCell(Defenders:"+neighbourCell.getDefendingTroopSize()+",P:"+neighbourCell.getOccupierKey()+")");

            if (neighbourCell.getOccupierKey() != playerKey) {
                return false;
            }
        }
        return true;
    }


    private boolean isNoMoreEmptyCell() {
        for (GameState.Cell cell : gameState.getCells()) {
            if (cell.isEmpty()) {
                return false;
            }
        }
        return true;
    }


    private int countPlayerCells(int playerKey) {
        int count = 0;
        for (GameState.Cell cell : gameState.getCells()) {
            if (cell.getOccupierKey() == playerKey) {
                count++;
            }
        }
        return count;
    }

    private int countAlivePlayers() {
        int count = 0;
        for (GameState.Opponent opponent : gameState.getOpponents()) {
            if (opponent.isAlive()) {
                count++;
            }
        }
        return count;
    }

    private GameState.Cell getCell(int cellKey) {
        GameState.Cell[] cells = gameState.getCells();
        return cells[cellKey];
    }

    private Set<GameState.Cell> getNeighboursOfCell(int cellKey) {
        Set<GameState.Cell> realNeighbours = new HashSet<>();

        boolean[] neighboursRow = gameState.getNeighboursMatrix()[cellKey];
        for (int neighbourKey = 0; neighbourKey < neighboursRow.length; neighbourKey++) {
            if (neighboursRow[neighbourKey] == true) {
                realNeighbours.add(getCell(neighbourKey));
            }
        }
        return realNeighbours;
    }

    private Set<Integer> getEmptyCellKeys() {
        Set<Integer> emptyCellKeys = new HashSet<>();
        GameState.Cell[] cells = gameState.getCells();

        for (int cellKey = 0; cellKey < cells.length; cellKey++) {
            if (cells[cellKey].isEmpty()) {
                emptyCellKeys.add(cellKey);
            }
        }
        return emptyCellKeys;
    }

    private GameState.Opponent getPlayer(int playerKey) {
        GameState.Opponent[] opponents = gameState.getOpponents();
        return opponents[playerKey];
    }

    public GameState.StatusCode getGameStatusCode() {
        return gameState.getStatusCode();
    }


    public static class Action {
        private int playerKey;
        private int targetCellKey;
        private int attackingTroopSize;

        public Action(int playerKey, int targetCellKey, int attackingTroopSize) {
            this.playerKey = playerKey;
            this.targetCellKey = targetCellKey;
            this.attackingTroopSize = attackingTroopSize;
        }

        public int getPlayerKey() {
            return playerKey;
        }


        public int getTargetCellKey() {
            return targetCellKey;
        }

        public int getAttackingTroopSize() {
            return attackingTroopSize;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Action action = (Action) o;
            return playerKey == action.playerKey;
        }

        @Override
        public int hashCode() {
            return Objects.hash(playerKey);
        }


        @Override
        public String toString() {
            return "Action{" +
                "playerKey=" + playerKey +
                ", targetCellKey=" + targetCellKey +
                ", attackingTroopSize=" + attackingTroopSize +
                '}';
        }
    }


}
