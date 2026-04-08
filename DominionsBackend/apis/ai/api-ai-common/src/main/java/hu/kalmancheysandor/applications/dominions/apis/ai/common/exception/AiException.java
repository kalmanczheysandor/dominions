package hu.kalmancheysandor.applications.dominions.apis.ai.common.exception;

public class AiException extends RuntimeException {
    public AiException() {
        super();
    }

    public AiException(String message) {
        super(message);
    }

    public AiException(String message, Throwable cause) {
        super(message, cause);
    }

    public AiException(Throwable cause) {
        super(cause);
    }

    public AiException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
