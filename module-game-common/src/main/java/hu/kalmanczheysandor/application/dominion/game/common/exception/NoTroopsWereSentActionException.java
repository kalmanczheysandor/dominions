package hu.kalmanczheysandor.application.dominion.game.common.exception;

public class NoTroopsWereSentActionException extends InvalidActionException {
    public NoTroopsWereSentActionException(int playerKey) {
        super(playerKey);
    }

    @Override
    public String toString() {
        return "NoTroopsWereSentActionException{" +
            "playerKey=" + playerKey +
            '}';
    }
}
