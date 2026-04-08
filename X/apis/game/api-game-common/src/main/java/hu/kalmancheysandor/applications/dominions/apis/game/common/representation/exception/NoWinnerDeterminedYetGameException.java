package hu.kalmancheysandor.applications.dominions.apis.game.common.representation.exception;


import hu.kalmancheysandor.applications.dominions.apis.game.common.representation.machine.exception.TGameStateMachineException;

public class NoWinnerDeterminedYetGameException extends TGameStateMachineException {


    @Override
    public String toString() {
        return "NoWinnerDeterminedYetGameException{}";
    }
}
