package hu.kalmancheysandor.application.dominion.api.game.common.engine.exception.action;

public class OutOfDoughnutAttackRangePlayerActionException extends PlayerActionException {
    private int targetedCellKey;

    public OutOfDoughnutAttackRangePlayerActionException(int playerKey, int targetedCellKey) {
        super(playerKey);
        this.targetedCellKey = targetedCellKey;
    }

    @Override
    public String toString() {
        return "OutOfDoughnutAttackRangePlayerActionException{" +
            "targetedCellKey=" + targetedCellKey +
            ", playerKey=" + playerKey +
            '}';
    }
}
