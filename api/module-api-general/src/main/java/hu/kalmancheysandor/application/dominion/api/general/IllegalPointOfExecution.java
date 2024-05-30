package hu.kalmancheysandor.application.dominion.api.general;

public class IllegalPointOfExecution extends RuntimeException{
    public IllegalPointOfExecution(String message) {
        super(message);
    }
}
