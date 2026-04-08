package hu.kalmancheysandor.applications.dominions.apis.game.common.representation.machine.exception.action;

public class NoTroopsWereSentGameStateMachineException extends TPlayerActionGameStateMachineException {
    public NoTroopsWereSentGameStateMachineException(int playerKey) {
        super(playerKey);
    }

    @Override
    public String toString() {
        return "NoTroopsWereSentPlayerActionOnGameStateException{" +
            "playerKey=" + getPlayerKey() +
            ", playerKey=" + getPlayerKey() +
            '}';
    }
}
