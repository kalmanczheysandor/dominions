package hu.kalmancheysandor.application.dominion.api.game.common.engine.exception;

public class PlayerAlreadyDeadGameException extends GameException {
    protected int playerKey;

    public PlayerAlreadyDeadGameException(int playerKey) {
        this.playerKey = playerKey;
    }

    @Override
    public String toString() {
        return "PlayerAlreadyDeadGameException{" +
            "playerKey=" + playerKey +
            '}';
    }

    public int getPlayerKey() {
        return playerKey;
    }
}
