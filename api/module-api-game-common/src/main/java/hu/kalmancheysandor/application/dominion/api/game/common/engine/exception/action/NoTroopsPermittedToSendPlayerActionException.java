package hu.kalmancheysandor.application.dominion.api.game.common.engine.exception.action;

public class NoTroopsPermittedToSendPlayerActionException extends PlayerActionException {
    public NoTroopsPermittedToSendPlayerActionException(int playerKey) {
        super(playerKey);
    }

    @Override
    public String toString() {
        return "NoTroopsPermittedToSendPlayerActionException{" +
            "playerKey=" + playerKey +
            '}';
    }
}
