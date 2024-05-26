package hu.kalmancheysandor.application.dominion.api.game.common.session.exception;

public class NotExistingInstanceSessionException extends SessionException {
    public NotExistingInstanceSessionException(String sessionKey) {
        super(sessionKey);
    }
}
