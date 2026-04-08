package hu.kalmancheysandor.applications.dominions.apis.game.common.orchestration.exception;

public class IntentionIsAlreadyGivenException extends TSessionException {
    public IntentionIsAlreadyGivenException(String sessionKey) {
        super(sessionKey);
    }
}
