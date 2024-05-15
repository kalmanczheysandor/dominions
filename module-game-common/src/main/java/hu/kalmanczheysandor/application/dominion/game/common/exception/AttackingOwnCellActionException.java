package hu.kalmanczheysandor.application.dominion.game.common.exception;

public class AttackingOwnCellActionException extends InvalidActionException{
    private int cellKey;

    public AttackingOwnCellActionException(int playerKey, int cellKey) {
        super(playerKey);
        this.cellKey = cellKey;
    }

    @Override
    public String toString() {
        return "AttackingOwnCellActionException{" +
            "cellKey=" + cellKey +
            ", playerKey=" + playerKey +
            '}';
    }
}
