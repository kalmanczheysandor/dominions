package hu.kalmancheysandor.application.dominion.api.game.common.session.exception;

public abstract class SessionException extends RuntimeException {
    private String sessionKey=null;
    public SessionException() {
    }

    public SessionException(String sessionKey) {
        this.sessionKey = sessionKey;
    }
    public String getSessionKey() {
        return sessionKey;
    }
}
