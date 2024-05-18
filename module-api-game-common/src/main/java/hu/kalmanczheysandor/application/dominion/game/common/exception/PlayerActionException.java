package hu.kalmanczheysandor.application.dominion.game.common.exception;

public class PlayerActionException extends GameException {
    protected int playerKey;

    public PlayerActionException(int playerKey) {
        this.playerKey = playerKey;
    }

    @Override
    public String toString() {
        return "PlayerActionException{" +
            "playerKey=" + playerKey +
            '}';
    }
}
