package hu.kalmanczheysandor.application.dominion.game.common.exception;

public class AttackingOwnCellPlayerActionException extends PlayerActionException {
    private int cellKey;

    public AttackingOwnCellPlayerActionException(int playerKey, int cellKey) {
        super(playerKey);
        this.cellKey = cellKey;
    }

    @Override
    public String toString() {
        return "AttackingOwnCellPlayerActionException{" +
            "cellKey=" + cellKey +
            ", playerKey=" + playerKey +
            '}';
    }
}
