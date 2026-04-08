package hu.kalmancheysandor.applications.dominions.apis.game.common.representation.state.exception;

public abstract class TGameStateException extends RuntimeException {
    public TGameStateException() {
    }

    public TGameStateException(String message) {
        super(message);
    }
}
