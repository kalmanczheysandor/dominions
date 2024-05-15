package hu.kalmanczheysandor.application.dominion.game.common.exception;

public class InvalidActionException extends GameException {
    protected int playerKey;

    public InvalidActionException(int playerKey) {
        this.playerKey = playerKey;
    }

    @Override
    public String toString() {
        return "InvalidActionException{" +
            "playerKey=" + playerKey +
            '}';
    }
}
