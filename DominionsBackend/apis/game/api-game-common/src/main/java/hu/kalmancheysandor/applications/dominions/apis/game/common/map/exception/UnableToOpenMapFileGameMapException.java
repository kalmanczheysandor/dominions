package hu.kalmancheysandor.applications.dominions.apis.game.common.map.exception;


public class UnableToOpenMapFileGameMapException extends TGameMapException {
    private String path;

    public UnableToOpenMapFileGameMapException(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
