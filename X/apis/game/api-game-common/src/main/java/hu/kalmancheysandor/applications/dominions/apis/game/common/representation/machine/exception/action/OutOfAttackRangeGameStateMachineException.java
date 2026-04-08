package hu.kalmancheysandor.applications.dominions.apis.game.common.representation.machine.exception.action;

public class OutOfAttackRangeGameStateMachineException extends TPlayerActionGameStateMachineException {
    private int targetedCellKey;

    public OutOfAttackRangeGameStateMachineException(int playerKey, int targetedCellKey) {
        super(playerKey);
        this.targetedCellKey = targetedCellKey;

    }

    public int getTargetedCellKey() {
        return targetedCellKey;
    }

    @Override
    public String toString() {
        return "OutOfAttackRangePlayerActionOnGameStateException{" +
            "targetedCellKey=" + getTargetedCellKey() +
            ", playerKey=" + getPlayerKey() +
            ", playerKey=" + getPlayerKey() +
            '}';
    }

}
