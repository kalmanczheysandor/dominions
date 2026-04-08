package hu.kalmancheysandor.applications.dominions.apis.game.common.representation.machine.exception.action;

public class TooMuchTroopsWereSentGameStateMachineException extends TPlayerActionGameStateMachineException {
    public TooMuchTroopsWereSentGameStateMachineException(int playerKey) {
        super(playerKey);
    }

    @Override
    public String toString() {
        return "TooMuchTroopsWereSentPlayerActionOnGameStateException{" +
            "playerKey=" + getPlayerKey() +
            ", playerKey=" + getPlayerKey() +
            '}';
    }
}
