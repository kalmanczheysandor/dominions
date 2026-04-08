package hu.kalmancheysandor.applications.dominions.apis.game.common.representation.machine.exception.action;

public class NotEnoughSupplyGameStateMachineException extends TPlayerActionGameStateMachineException {
    private int expectedSupplySize;
    private int availableSupplySize;

    public NotEnoughSupplyGameStateMachineException(int playerKey, int expectedSupplySize, int availableSupplySize) {
        super(playerKey);
        this.expectedSupplySize = expectedSupplySize;
        this.availableSupplySize = availableSupplySize;
    }

    @Override
    public String toString() {
        return "NotEnoughSupplyPlayerActionOnGameStateException{" +
            "expectedSupplySize=" + expectedSupplySize +
            ", availableSupplySize=" + availableSupplySize +
            ", playerKey=" + getPlayerKey() +
            '}';
    }
}
