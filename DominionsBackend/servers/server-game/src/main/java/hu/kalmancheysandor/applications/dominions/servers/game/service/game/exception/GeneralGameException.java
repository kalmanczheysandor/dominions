package hu.kalmancheysandor.applications.dominions.servers.game.service.game.exception;

public class GeneralGameException extends RuntimeException{
    private String message;

    public GeneralGameException(String message) {
        this.message = message;
    }
}
