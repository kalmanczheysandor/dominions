package hu.kalmancheysandor.applications.dominions.apis.game.common.representation.machine.exception.action;


import hu.kalmancheysandor.applications.dominions.apis.game.common.representation.machine.exception.TGameStateMachineException;

public abstract class TPlayerActionGameStateMachineException extends TGameStateMachineException {
    protected int playerKey;

    public TPlayerActionGameStateMachineException(int playerKey) {
        this.playerKey = playerKey;
    }

    @Override
    public String toString() {
        return "TPlayerActionOnGameStateException{" +
            "playerKey=" + getPlayerKey() +
            '}';
    }

    public int getPlayerKey() {
        return playerKey;
    }
}
