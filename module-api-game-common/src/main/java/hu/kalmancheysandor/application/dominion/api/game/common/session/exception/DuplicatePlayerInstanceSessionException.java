package hu.kalmancheysandor.application.dominion.api.game.common.session.exception;

public class DuplicatePlayerInstanceSessionException extends SessionException {
    private int playerId;

    public DuplicatePlayerInstanceSessionException(String sessionKey, int playerId) {
        super(sessionKey);
        this.playerId = playerId;
    }

    public int getPlayerId() {
        return playerId;
    }
}
