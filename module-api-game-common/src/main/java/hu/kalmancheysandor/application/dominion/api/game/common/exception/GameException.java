package hu.kalmancheysandor.application.dominion.api.game.common.exception;

public abstract class GameException extends RuntimeException {
    public GameException() {
    }

    public GameException(String message) {
        super(message);
    }
}
