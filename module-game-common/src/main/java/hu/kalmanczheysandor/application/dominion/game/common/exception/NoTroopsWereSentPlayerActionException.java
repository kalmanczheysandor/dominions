package hu.kalmanczheysandor.application.dominion.game.common.exception;

public class NoTroopsWereSentPlayerActionException extends PlayerActionException {
    public NoTroopsWereSentPlayerActionException(int playerKey) {
        super(playerKey);
    }

    @Override
    public String toString() {
        return "NoTroopsWereSentPlayerActionException{" +
            "playerKey=" + playerKey +
            '}';
    }
}
