package hu.kalmancheysandor.applications.dominions.apis.game.common.orchestration.exception;

public class NotExistingInstanceSessionException extends TSessionException {
    public NotExistingInstanceSessionException(String sessionKey) {
        super(sessionKey);
    }
}
