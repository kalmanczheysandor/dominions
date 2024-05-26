package hu.kalmancheysandor.application.dominion.api.game.common.session.exception;

public class PlayerKeyAlreadyIssuedSessionException extends SessionException {
    private int playerId;

    public PlayerKeyAlreadyIssuedSessionException(String sessionKey, int playerId) {
        super(sessionKey);
        this.playerId = playerId;
    }

    public int getPlayerId() {
        return playerId;
    }
}
