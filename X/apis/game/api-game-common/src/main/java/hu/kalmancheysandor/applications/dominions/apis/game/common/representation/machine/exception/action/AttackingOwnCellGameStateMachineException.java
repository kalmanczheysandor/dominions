package hu.kalmancheysandor.applications.dominions.apis.game.common.representation.machine.exception.action;


public class AttackingOwnCellGameStateMachineException extends TPlayerActionGameStateMachineException {
    private int cellKey;

    public AttackingOwnCellGameStateMachineException(int playerKey, int cellKey) {
        super(playerKey);
        this.cellKey = cellKey;
    }

    @Override
    public String toString() {
        return "AttackingOwnCellPlayerActionOnGameStateException{" +
            "cellKey=" + cellKey +
            ", playerKey=" + getPlayerKey() +
            ", playerKey=" + getPlayerKey() +
            '}';
    }
}
