package hu.kalmancheysandor.application.dominion.api.game.common.session.exception;

public class PlayerKeyNotNotMemberOfAiPlayerSlotSessionException extends SessionException {
    private int playerId;

    public PlayerKeyNotNotMemberOfAiPlayerSlotSessionException(String sessionKey, int playerId) {
        super(sessionKey);
        this.playerId = playerId;
    }

    public int getPlayerId() {
        return playerId;
    }
}
