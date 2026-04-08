package hu.kalmancheysandor.applications.dominions.apis.general.exceptions;

public class IllegalPointOfExecution extends RuntimeException {
    public IllegalPointOfExecution() {
    }

    public IllegalPointOfExecution(String message) {
        super(message);
    }
}
