package hu.kalmancheysandor.applications.dominions.apis.game.common.representation.machine.exception.action;


import hu.kalmancheysandor.applications.dominions.apis.game.common.representation.machine.exception.TGameStateMachineException;

public class GameAlreadyEndedGameStateMachineException extends TGameStateMachineException {
    @Override
    public String toString() {
        return "GameAlreadyEndedGameException{}";
    }
}
