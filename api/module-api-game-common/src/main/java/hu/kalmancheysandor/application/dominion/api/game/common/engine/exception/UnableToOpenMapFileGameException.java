package hu.kalmancheysandor.application.dominion.api.game.common.engine.exception;


public class UnableToOpenMapFileGameException extends GameException {
    private String path;

    public UnableToOpenMapFileGameException(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
