package hu.kalmancheysandor.application.dominion.server.game.exception;

public class PendingTurnSessionException extends RuntimeException {
    private String sessionKey;

    public PendingTurnSessionException(final String sessionKey) {
        this.sessionKey = sessionKey;
    }
    public String getSessionKey() {
        return sessionKey;
    }
}
