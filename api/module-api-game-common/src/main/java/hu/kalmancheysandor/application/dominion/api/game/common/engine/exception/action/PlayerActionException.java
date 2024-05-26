package hu.kalmancheysandor.application.dominion.api.game.common.engine.exception.action;

import hu.kalmancheysandor.application.dominion.api.game.common.engine.exception.GameException;

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
