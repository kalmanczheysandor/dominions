package hu.kalmancheysandor.application.dominion.server.game.exception;

public class NotExistingPlayerSessionException extends RuntimeException {
    private int playerId;

    public NotExistingPlayerSessionException(int playerId) {
        this.playerId = playerId;
    }
    public int getPlayerId() {
        return playerId;
    }
}
