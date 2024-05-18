package hu.kalmancheysandor.application.dominion.api.game.common.exception;

public class SelfAttackPlayerActionException extends PlayerActionException {
    private int targetedCellKey;

    public SelfAttackPlayerActionException(int playerKey, int targetedCellKey) {
        super(playerKey);
        this.targetedCellKey = targetedCellKey;
    }

    @Override
    public String toString() {
        return "SelfAttackPlayerActionException{" +
            "targetedCellKey=" + targetedCellKey +
            ", playerKey=" + playerKey +
            '}';
    }
}
