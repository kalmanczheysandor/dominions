package hu.kalmancheysandor.application.dominion.server.game.exception;

public class DuplicatePlayerInstanceSessionException extends RuntimeException {
    private int playerId;

    public DuplicatePlayerInstanceSessionException(int playerId) {
        this.playerId = playerId;
    }
    public int getPlayerId() {
        return playerId;
    }
}
