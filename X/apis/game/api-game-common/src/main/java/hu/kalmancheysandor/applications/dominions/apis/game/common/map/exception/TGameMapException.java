package hu.kalmancheysandor.applications.dominions.apis.game.common.map.exception;

public abstract class TGameMapException extends RuntimeException {
    public TGameMapException() {
    }

    public TGameMapException(String message) {
        super(message);
    }
}
