package hu.kalmancheysandor.applications.dominions.apis.game.common.representation.machine.exception;

public abstract class TGameStateMachineException extends RuntimeException {
    public TGameStateMachineException() {
    }

    public TGameStateMachineException(String message) {
        super(message);
    }
}
