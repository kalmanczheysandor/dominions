package hu.kalmancheysandor.application.dominion.api.game.common.session.exception;

public class PendingTurnSessionException extends SessionException {
    public PendingTurnSessionException(String sessionKey) {
        super(sessionKey);
    }
}
