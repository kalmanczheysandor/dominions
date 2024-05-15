package hu.kalmanczheysandor.application.dominion.game.common.exception;

public abstract class GameException extends RuntimeException {
    public GameException() {
    }

    public GameException(String message) {
        super(message);
    }
}
