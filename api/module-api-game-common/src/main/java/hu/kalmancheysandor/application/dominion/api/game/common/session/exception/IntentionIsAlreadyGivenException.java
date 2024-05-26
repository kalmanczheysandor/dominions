package hu.kalmancheysandor.application.dominion.api.game.common.session.exception;

public class IntentionIsAlreadyGivenException extends SessionException {
    public IntentionIsAlreadyGivenException(String sessionKey) {
        super(sessionKey);
    }
}
