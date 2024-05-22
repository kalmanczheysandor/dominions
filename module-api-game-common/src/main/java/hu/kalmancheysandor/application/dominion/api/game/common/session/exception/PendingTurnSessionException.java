package hu.kalmancheysandor.application.dominion.api.game.common.session.exception;

public class PendingTurnSessionException extends RuntimeException {
    private String sessionKey;

    public PendingTurnSessionException(final String sessionKey) {
        this.sessionKey = sessionKey;
    }
    public String getSessionKey() {
        return sessionKey;
    }
}
