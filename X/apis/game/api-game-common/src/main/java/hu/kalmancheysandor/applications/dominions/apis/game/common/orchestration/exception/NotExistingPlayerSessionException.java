package hu.kalmancheysandor.applications.dominions.apis.game.common.orchestration.exception;

public class NotExistingPlayerSessionException extends TSessionException {
    private int playerId;

    public NotExistingPlayerSessionException(String sessionKey, int playerId) {
        super(sessionKey);
        this.playerId = playerId;
    }

    public int getPlayerId() {
        return playerId;
    }
}
