package hu.kalmancheysandor.application.dominion.api.game.common.session.exception;

public class NotExistingPlayerSessionException extends RuntimeException {
    private int playerId;

    public NotExistingPlayerSessionException(int playerId) {
        this.playerId = playerId;
    }
    public int getPlayerId() {
        return playerId;
    }
}
