package hu.kalmanczheysandor.application.dominion.game.common.exception;

public class OutOfAttackRangeActionException extends InvalidActionException {
    private int targetedCellKey;

    public OutOfAttackRangeActionException(int playerKey, int targetedCellKey) {
        super(playerKey);
        this.targetedCellKey = targetedCellKey;
    }

    @Override
    public String toString() {
        return "OutOfAttackRangeActionException{" +
            "targetedCellKey=" + targetedCellKey +
            ", playerKey=" + playerKey +
            '}';
    }
}
