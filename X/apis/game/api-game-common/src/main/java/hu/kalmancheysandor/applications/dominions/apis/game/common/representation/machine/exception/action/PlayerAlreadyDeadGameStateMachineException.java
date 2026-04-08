package hu.kalmancheysandor.applications.dominions.apis.game.common.representation.machine.exception.action;

import hu.kalmancheysandor.applications.dominions.apis.game.common.representation.machine.exception.TGameStateMachineException;

public class PlayerAlreadyDeadGameStateMachineException extends TGameStateMachineException {
    protected int playerKey;

    public PlayerAlreadyDeadGameStateMachineException(int playerKey) {
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
