package hu.kalmancheysandor.application.dominion.api.game.common.engine;

import hu.kalmancheysandor.application.dominion.api.game.common.engine.exception.*;
import hu.kalmancheysandor.application.dominion.api.game.common.engine.exception.action.*;

import java.util.*;

public class GameEngine {
    private GameState gameState;

    public GameEngine() {
    }

    public GameEngine(GameState gameState) {
        this.gameState = gameState;
    }

    public GameState doAction(Set<Action> plannedActions, GameState gameState) {
        this.gameState = gameState;
        return doAction(plannedActions);
    }

    public GameState doAction(Set<Action> plannedActions) {
        //if (!isMoreActionPossible()) {
        if (isEndOfGame()) {
            throw new EndOfGameException();

        }

        // Validations
        validatePlayersAction(plannedActions);

        // Grouping and resolving conflicting actions
        Map<Integer, Set<Action>> conflictGroups = new HashMap<>();
        for (Action observedAction : plannedActions) {

            if (!conflictGroups.containsKey(observedAction.getTargetCellKey())) {// The first action in the group
                Set<Action> actions = new HashSet<>();
                actions.add(observedAction);
                conflictGroups.put(observedAction.getTargetCellKey(), actions);
            } else { // After the first action in the group
                Set<Action> actions = conflictGroups.get(observedAction.getTargetCellKey());
                actions.add(observedAction);
            }
        }
        //  System.out.println(group);

        // Resolving conflict groups. Find the highest offer in each group
        // Under each index where more than one action are stored, conflict resolution has to be performed.
        Set<Action> actionsToProcess = new HashSet<>();
        for (Set<Action> actions : conflictGroups.values()) {
            Action a = resolveTheConflict(actions);
            if (a != null) {
                actionsToProcess.add(a);
            }
        }

        // Calculate the outcome of steps
        for (Action processedAction : actionsToProcess) {
            GameState.Cell attackedCell = getCell(processedAction.getTargetCellKey());

            if (processedAction.getTargetCellKey() != null) {
                if (attackedCell.getDefendingTroopSize() < processedAction.getAttackingTroopSize()) {
                    attackedCell.setDefendingTroopSize(processedAction.getAttackingTroopSize() - attackedCell.getDefendingTroopSize());
                    attackedCell.setOccupierKey(processedAction.getPlayerKey());
                } else if (attackedCell.getDefendingTroopSize() > processedAction.getAttackingTroopSize()) {
                    attackedCell.setDefendingTroopSize(attackedCell.getDefendingTroopSize() - processedAction.getAttackingTroopSize());
                } else {
                    attackedCell.setDefendingTroopSize(0);
                    attackedCell.setOccupierKey(-1);
                }
            } else if (processedAction.getTargetCellKey() == null) {

            }
        }

        // Decreasing reserve
        for (Action observedAction : plannedActions) {
            GameState.Opponent attackingPlayer = getPlayer(observedAction.getPlayerKey());
            attackingPlayer.setReserveSize(attackingPlayer.getReserveSize() - observedAction.getAttackingTroopSize());
        }

        // Removal of the weakest player in case of no more empty cell
        if (isNoMoreEmptyCell() && countAlivePlayers() > 1) {
            Integer weakestPlayerKey = findTheWeakestPlayer();
            if (weakestPlayerKey != null) {
                demolishPlayerDominion(weakestPlayerKey);
                getPlayer(weakestPlayerKey).setAlive(false);
            }

            if (countAlivePlayers() == 1) {
                Integer winnerKey = null;
                GameState.Opponent[] opponents = gameState.getOpponents();
                for (int playerKey = 0; playerKey < opponents.length; playerKey++) {
                    if (opponents[playerKey].isAlive()) {
                        winnerKey = playerKey;
                    }
                }

                occupyEverythingForTheWinner(winnerKey);
                gameState.setWinnerKey(winnerKey);
                gameState.setStatusCode(GameState.StatusCode.FINISHED);
            }
        }

        if (!isEndOfGame()) {
            incrementAllReserve();
            gameState.setStatusCode(GameState.StatusCode.PROCEEDED);
        }

        return gameState;
    }

    public static void validatePlayerAction(GameState state, Action action) {
        int playerKey = action.getPlayerKey();
        Integer targetCellKey = action.getTargetCellKey();
        System.out.print("VALIDATE");
        System.out.print("-targerCellKey:" + targetCellKey);
        System.out.print("-attackingTroopSize:" + action.getAttackingTroopSize());
        if (targetCellKey != null) {       // When itt is an attack action
            GameState.Cell targetCell = state.getCell(targetCellKey);
            GameState.Opponent player = state.getOpponent(playerKey);

            // When it is an attack without troops
            if (action.getAttackingTroopSize() <= 0) {
                throw new NoTroopsWereSentPlayerActionException(playerKey);
            }

            // When it is an attack without troops
            if (action.getAttackingTroopSize() > 100) {
                throw new TooMuchTroopsWereSentPlayerActionException(playerKey);
            }

            // When it is an attack but the army size is over calculated
            if (player.getReserveSize() < action.getAttackingTroopSize()) {
                throw new NotEnoughSupplyPlayerActionException(playerKey, action.getAttackingTroopSize(), player.getReserveSize());
            }

            // When the cell attacked belongs to the attacker and neither to the enemy and nor empty.
            if (targetCell.getOccupierKey() == action.getPlayerKey()) {
                throw new SelfAttackPlayerActionException(playerKey, targetCellKey);
            }

            if (!isCellANeighbourOfPlayer(state, action.getPlayerKey(), action.getTargetCellKey())) {
                throw new OutOfAttackRangePlayerActionException(action.getPlayerKey(), action.getTargetCellKey());
            }

            if (isPlayerCausingDoughnutEffect(state, action.getPlayerKey())) {
//                System.out.println("DOUGHNUT ? ["+action.getPlayerKey()+"] >> yes ");
                if (!isCellAnEmptyNeighbourOfPlayer(state, action.getPlayerKey(), action.getTargetCellKey())) {
                    //throw new OutOfDoughnutAttackRangePlayerActionException(action.getPlayerKey(), action.getTargetCellKey());
                }
            }

        } else {

            if (action.getAttackingTroopSize() != 0) {
                throw new NoTroopsPermittedToSendPlayerActionException(playerKey);
            }
        }

    }

    private void validatePlayersAction(Set<Action> actions) {
        GameState.Cell targetCell;
        GameState.Opponent player;
        int playerKey;
        int targetCellKey;


        for (Action action : actions) {
            validatePlayerAction(gameState, action);

//            playerKey = action.getPlayerKey();
//            targetCellKey = action.getTargetCellKey();
//
//            targetCell = getCell(targetCellKey);
//            player = getPlayer(playerKey);
//
//            // When it is an attack without troops
//            if (action.getAttackingTroopSize() == 0) {
//                throw new NoTroopsWereSentPlayerActionException(playerKey);
//            }
//
//            // When it is an attack but the army size is overcalculated
//            if (player.getReserveSize() < action.getAttackingTroopSize()) {
//                throw new NotEnoughSupplyPlayerActionException(playerKey, action.getAttackingTroopSize(), player.getReserveSize());
//            }
//
//            // When the cell attacked belongs to the attacker and neither to the enemy and nor empty.
//            if (targetCell.getOccupierKey() == action.getPlayerKey()) {
////                System.out.println("SElf attack: cellKey:" + targetCellKey + " " + targetCell.getOccupierKey() + " - " + action.getPlayerKey());
//                throw new SelfAttackPlayerActionException(playerKey, targetCellKey);
//            }
//
//            if (!isCellANeighbourOfPlayer(action.getPlayerKey(), action.getTargetCellKey())) {
//                throw new OutOfAttackRangePlayerActionException(action.getPlayerKey(), action.getTargetCellKey());
//            }
//
//
//            if (isPlayerCausingDoughnutEffect(action.getPlayerKey())) {
////                System.out.println("DOUGHNUT ? ["+action.getPlayerKey()+"] >> yes ");
//                if (!isCellAnEmptyNeighbourOfPlayer(action.getPlayerKey(), action.getTargetCellKey())) {
//                    throw new OutOfDoughnutAttackRangePlayerActionException(action.getPlayerKey(), action.getTargetCellKey());
//                }
//            } else {
////                System.out.println("DOUGHNUT ? ["+action.getPlayerKey()+"] >> no ");
//            }

        }
    }

    private Action resolveTheConflict(Set<Action> actions) {
        int highestValue = 0;
        int count = 0;
        Action a = null;
        for (Action action : actions) {
            if (action.getAttackingTroopSize() == highestValue) {
                count++;
                a = null;
            } else if (action.getAttackingTroopSize() > highestValue) {
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
        gameState.getOpponents()[1].incrementReserveSize(0);
    }

    private void demolishPlayerDominion(int playerKey) {
        for (GameState.Cell cell : gameState.getCells()) {
            if (cell.getOccupierKey() == playerKey) {
                cell.free();
            }
        }
    }

    private void occupyEverythingForTheWinner(int playerKey) {
        System.out.println("occupyEverythingForTheWinner");

        for (GameState.Cell cell : gameState.getCells()) {
            if (cell.isEmpty() || cell.getOccupierKey() != playerKey) {
                cell.setOccupierKey(playerKey);
                cell.setDefendingTroopSize(0);
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

            if (player.isAlive()) {
                //System.out.println("--------" + playerKey+"--------");
                int dominionSize = countPlayerCells(playerKey);

//                System.out.println("PlayerIndex:" + playerKey);
//                System.out.println("dominionSize:" + dominionSize);


                if (dominionSize == weakestValue) {
                    foundCount++;
                    weakestKey = null;
                } else if (dominionSize < weakestValue) {
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


    private boolean isNoMoreEmptyCell() {
        for (GameState.Cell cell : gameState.getCells()) {
            if (cell.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private boolean isMoreActionPossible() {
        System.out.println("isMoreActionPossible");
        if (isNoMoreEmptyCell()) {
            System.out.println("---isNoMoreEmptyCell");
        }

        if (isNoMoreEmptyCell() && countAlivePlayers() <= 2) {
            return false;
        }
        return true;
    }

    public boolean isEndOfGame() {
        if (gameState.getStatusCode() == GameState.StatusCode.FINISHED) {
            return true;
        }
        return false;
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

    private static Set<GameState.Cell> getNeighboursOfCell(GameState state, int cellKey) {
        Set<GameState.Cell> realNeighbours = new HashSet<>();

        boolean[] neighboursRow = state.getNeighboursMatrix()[cellKey];
        for (int neighbourKey = 0; neighbourKey < neighboursRow.length; neighbourKey++) {
            if (neighboursRow[neighbourKey] == true) {
                realNeighbours.add(state.getCell(neighbourKey));
            }
        }
        return realNeighbours;
    }

//    private Set<Integer> getEmptyCellKeys() {
//        Set<Integer> emptyCellKeys = new HashSet<>();
//        GameState.Cell[] cells = gameState.getCells();
//
//        for (int cellKey = 0; cellKey < cells.length; cellKey++) {
//            if (cells[cellKey].isEmpty()) {
//                emptyCellKeys.add(cellKey);
//            }
//        }
//        return emptyCellKeys;
//    }

    private GameState.Opponent getPlayer(int playerKey) {
        GameState.Opponent[] opponents = gameState.getOpponents();
        return opponents[playerKey];
    }

    public GameState.StatusCode getGameStatusCode() {
        return gameState.getStatusCode();
    }

    public GameState getGameState() {
        return this.gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }


    private static boolean isCellANeighbourOfPlayer(GameState state, int playerKey, int observedCellKey) {
        GameState.Cell observedCell = state.getCell(observedCellKey);

        // Any cell occupied by player is not a counted as a neighbour of that player
        if (observedCell.getOccupierKey() == playerKey) {
            return false;
        }

        for (GameState.Cell neighbourCell : getNeighboursOfCell(state, observedCellKey)) {
            if (neighbourCell.getOccupierKey() == playerKey) { // When the neighbour cell is occupied by the player
                return true;
            }
        }
        return false;
    }

    private static boolean isCellAnEmptyNeighbourOfPlayer(GameState state, int playerKey, int cellKey) {
        GameState.Cell observedCell = state.getCell(cellKey);

        // Any cell occupied by the player is not a counted as a neighbour of that player
        if (observedCell.getOccupierKey() == playerKey) {
            return false;
        }

        // Cell must be empty
        if (!observedCell.isEmpty()) {
            return false;
        }


        for (GameState.Cell neighbourCell : getNeighboursOfCell(state, cellKey)) {
            if (neighbourCell.getOccupierKey() == playerKey) { // When the neighbour cell is occupied by the player
                return true;
            }
        }
        return false;
    }

    private static boolean isPlayerCausingDoughnutEffect(GameState state, int playerKey) {
        for (int emptyCellKey : state.getEmptyCellKeys()) {
            if (isCellBlockedByPlayer(state, emptyCellKey, playerKey)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isCellBlockedByPlayer(GameState state, int cellKey, int playerKey) {
        for (GameState.Cell neighbourCell : getNeighboursOfCell(state, cellKey)) {
            if (neighbourCell.getOccupierKey() != playerKey) {
                return false;
            }
        }
        return true;
    }


    public static class Action {
        private int playerKey;
        private Integer targetCellKey;
        private int attackingTroopSize;

        public Action(int playerKey, Integer targetCellKey, int attackingTroopSize) {
            this.playerKey = playerKey;
            this.targetCellKey = targetCellKey;
            this.attackingTroopSize = attackingTroopSize;
        }

        public int getPlayerKey() {
            return playerKey;
        }


        public Integer getTargetCellKey() {
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
