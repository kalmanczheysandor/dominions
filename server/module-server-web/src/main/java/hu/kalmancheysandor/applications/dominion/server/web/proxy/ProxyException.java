package hu.kalmancheysandor.applications.dominion.server.web.proxy;

import hu.kalmancheysandor.application.dominion.server.game.exceptionhandling.ErrorDetails;

public class ProxyException extends RuntimeException {
    private ErrorDetails errorDetails;

    public ProxyException(ErrorDetails errorDetails) {
        this.errorDetails = errorDetails;
    }

    public ErrorDetails getErrorDetails() {
        return errorDetails;
    }
}
