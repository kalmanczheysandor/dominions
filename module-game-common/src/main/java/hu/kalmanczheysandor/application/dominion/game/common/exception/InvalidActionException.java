package hu.kalmanczheysandor.application.dominion.game.common.exception;

public class InvalidActionException extends GameException{
    private int playerKey;

    public InvalidActionException(int playerKey) {
        this.playerKey = playerKey;
    }
}
