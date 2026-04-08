package hu.kalmancheysandor.applications.dominions.apis.game.common.representation.machine.exception.action;

public class OutOfDoughnutAttackRangeGameStateMachineException extends TPlayerActionGameStateMachineException {
    private int targetedCellKey;

    public OutOfDoughnutAttackRangeGameStateMachineException(int playerKey, int targetedCellKey) {
        super(playerKey);
        this.targetedCellKey = targetedCellKey;
    }

    @Override
    public String toString() {
        return "OutOfDoughnutAttackRangePlayerActionOnGameStateException{" +
            "targetedCellKey=" + targetedCellKey +
            ", playerKey=" + getPlayerKey() +
            ", playerKey=" + getPlayerKey() +
            '}';
    }
}
