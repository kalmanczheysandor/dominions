package hu.kalmancheysandor.applications.dominions.apis.game.common.orchestration.exception;

public abstract class TSessionException extends RuntimeException {
    private String sessionKey=null;
    public TSessionException() {
    }

    public TSessionException(String sessionKey) {
        this.sessionKey = sessionKey;
    }
    public String getSessionKey() {
        return sessionKey;
    }
}
