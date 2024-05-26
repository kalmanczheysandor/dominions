package hu.kalmancheysandor.application.dominion.api.game.common.session.exception;

public class PlayerKeyNotNotMemberOfHumanPlayerSlotSessionException extends SessionException {
    private int playerId;

    public PlayerKeyNotNotMemberOfHumanPlayerSlotSessionException(String sessionKey, int playerId) {
        super(sessionKey);
        this.playerId = playerId;
    }

    public int getPlayerId() {
        return playerId;
    }
}
