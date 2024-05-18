package hu.kalmancheysandor.application.dominion.api.game.common.exception;

public class NotEnoughSupplyPlayerActionException extends PlayerActionException {
    private int expectedSupplySize;
    private int availableSupplySize;

    public NotEnoughSupplyPlayerActionException(int playerKey, int expectedSupplySize, int availableSupplySize) {
        super(playerKey);
        this.expectedSupplySize = expectedSupplySize;
        this.availableSupplySize = availableSupplySize;
    }

    @Override
    public String toString() {
        return "NotEnoughSupplyPlayerActionException{" +
            "expectedSupplySize=" + expectedSupplySize +
            ", availableSupplySize=" + availableSupplySize +
            ", playerKey=" + playerKey +
            '}';
    }
}
