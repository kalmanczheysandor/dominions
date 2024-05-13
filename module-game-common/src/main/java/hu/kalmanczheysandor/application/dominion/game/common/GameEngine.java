package hu.kalmanczheysandor.application.dominion.game.common;

import hu.kalmanczheysandor.application.dominion.game.common.exception.NoTroopsWereSentActionException;
import hu.kalmanczheysandor.application.dominion.game.common.exception.NotEnoughSupplyActionException;
import hu.kalmanczheysandor.application.dominion.game.common.exception.NotValidTargetActionException;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class GameEngine {
    private GameState gameState;

    public GameEngine(GameState gameState) {
        this.gameState = gameState;
    }


    public GameState doAction(Set<Action> plannedActions) {
        // Validations
        validatePlayersAction(plannedActions);


//        GameState.Cell targetCell;
//        GameState.Opponent player;
//        int playerKey;
//        int targetCellKey;


        // Non-conflicting targets
        Set<Action> actionsToProcess = new HashSet<>(plannedActions);
        Set<Action> remove = new HashSet<>();
        for (Action processedAction : actionsToProcess) {
            if (aimingSameCell(processedAction.getTargetCellKey(), plannedActions) == 1) {
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
                remove.add(processedAction);
            }
        }
        actionsToProcess.removeAll(remove);






        incrementAllReserve();
        return gameState;
    }


    private int aimingSameCell(int targetedCellKey, Set<Action> actions) {
        int count = 0;
        for (Action action : actions) {
            if (action.getTargetCellKey() == targetedCellKey) {
                count++;
            }
        }
        return count;
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

            // Check whether is action to a valid target
            if (targetCell.getOccupierKey() == action.getPlayerKey()) {
                throw new NotValidTargetActionException(playerKey, targetCellKey);
            }

            // No troops were sent
            if (action.getAttackingTroopSize() == 0) {
                throw new NoTroopsWereSentActionException(playerKey);
            }

            //
            if (player.getReserveSize() < action.getAttackingTroopSize()) {
                throw new NotEnoughSupplyActionException(playerKey, action.getAttackingTroopSize(), player.getReserveSize());
            }
        }
    }

    private void incrementAllReserve() {
        for (GameState.Opponent player : gameState.getOpponents()) {
            player.incrementReserveSize(1);
        }
    }


//
//    public boolean isCellOwnedByPlayer(int cellKey, int playerKey) {
//        for (GameState.Cell cell : gameState.getCells()) {
//            cell.getOccupierKey()
//        }
//    }

    private GameState.Cell getCell(int cellKey) {
        GameState.Cell[] cells = gameState.getCells();
        return cells[cellKey];
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
    }


}
