package hu.kalmancheysandor.applications.dominions.apis.ai.general.common.exception;

public class LandKeyNotExistAiException extends AiException{
    private int landKey;

    public LandKeyNotExistAiException(int landKey) {
        this.landKey = landKey;
    }

    public LandKeyNotExistAiException(String message, int landKey) {
        super(message);
        this.landKey = landKey;
    }

    public LandKeyNotExistAiException(String message, Throwable cause, int landKey) {
        super(message, cause);
        this.landKey = landKey;
    }

    public LandKeyNotExistAiException(Throwable cause, int landKey) {
        super(cause);
        this.landKey = landKey;
    }

    public LandKeyNotExistAiException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, int landKey) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.landKey = landKey;
    }
}
