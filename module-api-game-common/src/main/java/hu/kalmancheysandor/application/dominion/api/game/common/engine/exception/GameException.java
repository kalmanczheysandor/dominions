package hu.kalmancheysandor.application.dominion.api.game.common.engine.exception;

public abstract class GameException extends RuntimeException {
    public GameException() {
    }

    public GameException(String message) {
        super(message);
    }
}
