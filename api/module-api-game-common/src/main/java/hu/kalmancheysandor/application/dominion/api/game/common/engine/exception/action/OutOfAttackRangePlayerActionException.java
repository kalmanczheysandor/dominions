package hu.kalmancheysandor.application.dominion.api.game.common.engine.exception.action;

public class OutOfAttackRangePlayerActionException extends PlayerActionException {
    private int targetedCellKey;

    public OutOfAttackRangePlayerActionException(int playerKey, int targetedCellKey) {
        super(playerKey);
        this.targetedCellKey = targetedCellKey;

    }

    public int getTargetedCellKey() {
        return targetedCellKey;
    }

    @Override
    public String toString() {
        return "OutOfAttackRangePlayerActionException{" +
            "targetedCellKey=" + targetedCellKey +
            ", playerKey=" + playerKey +
            '}';
    }

}
