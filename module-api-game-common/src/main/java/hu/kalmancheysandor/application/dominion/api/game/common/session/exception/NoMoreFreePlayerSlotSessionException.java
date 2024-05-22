package hu.kalmancheysandor.application.dominion.api.game.common.session.exception;

public class NoMoreFreePlayerSlotSessionException extends SessionException {
    public NoMoreFreePlayerSlotSessionException(String sessionKey) {
        super(sessionKey);
    }
}
