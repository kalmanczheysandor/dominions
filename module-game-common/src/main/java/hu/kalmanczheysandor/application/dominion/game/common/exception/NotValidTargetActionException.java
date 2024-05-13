package hu.kalmanczheysandor.application.dominion.game.common.exception;

public class NotValidTargetActionException extends InvalidActionException {
    private int targetedCellKey;

    public NotValidTargetActionException(int playerKey, int targetedCellKey) {
        super(playerKey);
        this.targetedCellKey = targetedCellKey;
    }
}
