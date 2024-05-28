package hu.kalmancheysandor.application.dominion.api.game.common.engine.exception.action;

public class TooMuchTroopsWereSentPlayerActionException extends PlayerActionException {
    public TooMuchTroopsWereSentPlayerActionException(int playerKey) {
        super(playerKey);
    }

    @Override
    public String toString() {
        return "TooMuchTroopsWereSentPlayerActionException{" +
            "playerKey=" + playerKey +
            '}';
    }
}
