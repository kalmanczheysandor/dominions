package hu.kalmanczheysandor.application.dominion.game.common.exception;

public class NoWinnerDeterminedYetGameException extends GameException {
    public NoWinnerDeterminedYetGameException() {
    }

    @Override
    public String toString() {
        return "NoWinnerDeterminedYetGameException{}";
    }
}
