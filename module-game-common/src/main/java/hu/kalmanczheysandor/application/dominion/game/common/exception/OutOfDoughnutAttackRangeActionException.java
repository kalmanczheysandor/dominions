package hu.kalmanczheysandor.application.dominion.game.common.exception;

public class OutOfDoughnutAttackRangeActionException extends InvalidActionException {
    private int targetedCellKey;

    public OutOfDoughnutAttackRangeActionException(int playerKey, int targetedCellKey) {
        super(playerKey);
        this.targetedCellKey = targetedCellKey;
    }

    @Override
    public String toString() {
        return "OutOfDoughnutAttackRangeActionException{" +
            "targetedCellKey=" + targetedCellKey +
            ", playerKey=" + playerKey +
            '}';
    }
}
