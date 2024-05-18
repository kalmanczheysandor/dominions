package hu.kalmancheysandor.application.dominion.api.game.common.exception;

public class NoWinnerDeterminedYetGameException extends GameException {
    public NoWinnerDeterminedYetGameException() {
    }

    @Override
    public String toString() {
        return "NoWinnerDeterminedYetGameException{}";
    }
}
