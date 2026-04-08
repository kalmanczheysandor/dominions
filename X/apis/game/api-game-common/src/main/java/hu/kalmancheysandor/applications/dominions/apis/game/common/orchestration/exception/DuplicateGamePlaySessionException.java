package hu.kalmancheysandor.applications.dominions.apis.game.common.orchestration.exception;

public class DuplicateGamePlaySessionException extends TSessionException {
    public DuplicateGamePlaySessionException(String sessionKey) {
        super(sessionKey);
    }
}
