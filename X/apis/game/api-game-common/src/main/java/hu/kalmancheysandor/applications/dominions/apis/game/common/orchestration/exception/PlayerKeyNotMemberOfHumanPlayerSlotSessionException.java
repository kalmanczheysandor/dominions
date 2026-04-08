package hu.kalmancheysandor.applications.dominions.apis.game.common.orchestration.exception;

public class PlayerKeyNotMemberOfHumanPlayerSlotSessionException extends TSessionException {
    private int playerId;

    public PlayerKeyNotMemberOfHumanPlayerSlotSessionException(String sessionKey, int playerId) {
        super(sessionKey);
        this.playerId = playerId;
    }

    public int getPlayerId() {
        return playerId;
    }
}
