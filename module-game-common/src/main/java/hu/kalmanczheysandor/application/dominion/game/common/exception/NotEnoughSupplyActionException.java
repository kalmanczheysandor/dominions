package hu.kalmanczheysandor.application.dominion.game.common.exception;

public class NotEnoughSupplyActionException extends InvalidActionException {
    private int expectedSupplySize;
    private int availableSupplySize;

    public NotEnoughSupplyActionException(int playerKey, int expectedSupplySize, int availableSupplySize) {
        super(playerKey);
        this.expectedSupplySize = expectedSupplySize;
        this.availableSupplySize = availableSupplySize;
    }
}
