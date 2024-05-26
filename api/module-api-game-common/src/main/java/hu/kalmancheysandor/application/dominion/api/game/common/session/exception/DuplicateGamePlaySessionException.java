package hu.kalmancheysandor.application.dominion.api.game.common.session.exception;

public class DuplicateGamePlaySessionException extends SessionException {
    public DuplicateGamePlaySessionException(String sessionKey) {
        super(sessionKey);
    }
}
