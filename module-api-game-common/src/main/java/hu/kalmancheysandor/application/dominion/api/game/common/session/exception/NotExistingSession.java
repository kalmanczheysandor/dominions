package hu.kalmancheysandor.application.dominion.api.game.common.session.exception;

public class NotExistingSession extends RuntimeException {
    private String sessionId;

    public NotExistingSession(final String sessionId) {
        this.sessionId = sessionId;
    }
    public String getSessionId() {
        return sessionId;
    }
}
