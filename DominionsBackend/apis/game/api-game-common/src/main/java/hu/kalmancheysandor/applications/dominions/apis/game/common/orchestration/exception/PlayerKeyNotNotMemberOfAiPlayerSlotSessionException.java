package hu.kalmancheysandor.applications.dominions.apis.game.common.orchestration.exception;

public class PlayerKeyNotNotMemberOfAiPlayerSlotSessionException extends TSessionException {
    private int playerId;

    public PlayerKeyNotNotMemberOfAiPlayerSlotSessionException(String sessionKey, int playerId) {
        super(sessionKey);
        this.playerId = playerId;
    }

    public int getPlayerId() {
        return playerId;
    }
}
