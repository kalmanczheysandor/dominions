package hu.kalmancheysandor.application.dominion.api.game.common.session.exception;

public class DuplicatePlayerInstanceSessionException extends RuntimeException {
    private int playerId;

    public DuplicatePlayerInstanceSessionException(int playerId) {
        this.playerId = playerId;
    }
    public int getPlayerId() {
        return playerId;
    }
}
