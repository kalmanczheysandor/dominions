package hu.kalmancheysandor.applications.dominions.apis.game.common.orchestration.exception;

public class NoMoreFreePlayerSlotSessionException extends TSessionException {
    public NoMoreFreePlayerSlotSessionException(String sessionKey) {
        super(sessionKey);
    }
}
