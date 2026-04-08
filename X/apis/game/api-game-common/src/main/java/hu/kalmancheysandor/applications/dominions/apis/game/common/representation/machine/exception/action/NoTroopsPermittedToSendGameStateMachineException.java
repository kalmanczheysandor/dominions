package hu.kalmancheysandor.applications.dominions.apis.game.common.representation.machine.exception.action;

public class NoTroopsPermittedToSendGameStateMachineException extends TPlayerActionGameStateMachineException {
    public NoTroopsPermittedToSendGameStateMachineException(int playerKey) {
        super(playerKey);
    }

    @Override
    public String toString() {
        return "NoTroopsPermittedToSendPlayerActionOnGameStateException{" +
            "playerKey=" + getPlayerKey() +
            ", playerKey=" + getPlayerKey() +
            '}';
    }
}
