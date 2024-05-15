package hu.kalmanczheysandor.application.dominion.game.common.exception;

public class SelfAttackActionException extends InvalidActionException {
    private int targetedCellKey;

    public SelfAttackActionException(int playerKey, int targetedCellKey) {
        super(playerKey);
        this.targetedCellKey = targetedCellKey;
    }

    @Override
    public String toString() {
        return "SelfAttackActionException{" +
            "targetedCellKey=" + targetedCellKey +
            ", playerKey=" + playerKey +
            '}';
    }
}
