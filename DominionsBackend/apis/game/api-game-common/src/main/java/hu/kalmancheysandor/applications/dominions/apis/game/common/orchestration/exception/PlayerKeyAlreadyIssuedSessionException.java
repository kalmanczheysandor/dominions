package hu.kalmancheysandor.applications.dominions.apis.game.common.orchestration.exception;

public class PlayerKeyAlreadyIssuedSessionException extends TSessionException {
    private int playerId;

    public PlayerKeyAlreadyIssuedSessionException(String sessionKey, int playerId) {
        super(sessionKey);
        this.playerId = playerId;
    }

    public int getPlayerId() {
        return playerId;
    }
}
