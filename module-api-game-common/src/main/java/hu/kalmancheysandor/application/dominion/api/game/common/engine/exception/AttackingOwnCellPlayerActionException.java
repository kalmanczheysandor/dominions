package hu.kalmancheysandor.application.dominion.api.game.common.engine.exception;

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
