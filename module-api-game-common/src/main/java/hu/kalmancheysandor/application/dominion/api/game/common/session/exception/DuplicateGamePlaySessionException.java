package hu.kalmancheysandor.application.dominion.api.game.common.session.exception;

public class DuplicateGamePlaySessionException extends RuntimeException {
   private String sessionKey;

    public DuplicateGamePlaySessionException(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public String getSessionKey() {
        return sessionKey;
    }
}
