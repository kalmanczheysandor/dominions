package hu.kalmancheysandor.application.dominion.api.game.common.exception;

public class EndOfGameException extends GameException {
    public EndOfGameException() {
    }

    @Override
    public String toString() {
        return "EndOfGameException{}";
    }
}
