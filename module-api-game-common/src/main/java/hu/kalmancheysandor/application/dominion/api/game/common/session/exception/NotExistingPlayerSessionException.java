package hu.kalmancheysandor.application.dominion.api.game.common.session.exception;

public class NotExistingPlayerSessionException extends SessionException {
    private int playerId;

    public NotExistingPlayerSessionException(String sessionKey, int playerId) {
        super(sessionKey);
        this.playerId = playerId;
    }

    public int getPlayerId() {
        return playerId;
    }
}
