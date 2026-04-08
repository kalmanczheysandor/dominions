package hu.kalmancheysandor.applications.dominions.apis.game.common.orchestration.exception;

public class PendingTurnSessionException extends TSessionException {
    public PendingTurnSessionException(String sessionKey) {
        super(sessionKey);
    }
}
