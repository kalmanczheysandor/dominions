package hu.kalmancheysandor.application.dominion.server.game.exception;

public class DuplicateGamePlaySessionException extends RuntimeException {
   private String sessionKey;

    public DuplicateGamePlaySessionException(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public String getSessionKey() {
        return sessionKey;
    }
}
