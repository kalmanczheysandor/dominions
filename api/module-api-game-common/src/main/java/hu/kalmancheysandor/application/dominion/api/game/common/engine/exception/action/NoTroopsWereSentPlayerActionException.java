package hu.kalmancheysandor.application.dominion.api.game.common.engine.exception.action;

public class NoTroopsWereSentPlayerActionException extends PlayerActionException {
    public NoTroopsWereSentPlayerActionException(int playerKey) {
        super(playerKey);
    }

    @Override
    public String toString() {
        return "NoTroopsWereSentPlayerActionException{" +
            "playerKey=" + playerKey +
            '}';
    }
}
