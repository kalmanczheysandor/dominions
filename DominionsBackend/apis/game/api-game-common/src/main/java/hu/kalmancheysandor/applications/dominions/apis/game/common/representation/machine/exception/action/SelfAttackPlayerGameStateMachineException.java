package hu.kalmancheysandor.applications.dominions.apis.game.common.representation.machine.exception.action;

public class SelfAttackPlayerGameStateMachineException extends TPlayerActionGameStateMachineException {
    private int targetedCellKey;

    public SelfAttackPlayerGameStateMachineException(int playerKey, int targetedCellKey) {
        super(playerKey);
        this.targetedCellKey = targetedCellKey;
    }

    @Override
    public String toString() {
        return "SelfAttackPlayerActionOnGameStateException{" +
            "targetedCellKey=" + targetedCellKey +
            ", playerKey=" + getPlayerKey() +
            '}';
    }
}
