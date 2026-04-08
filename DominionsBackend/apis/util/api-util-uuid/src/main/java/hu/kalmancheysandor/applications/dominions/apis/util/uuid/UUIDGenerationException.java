package hu.kalmancheysandor.applications.dominions.apis.util.uuid;

public class UUIDGenerationException extends RuntimeException{
    private String entityName;
    private int attemptCount;

    public UUIDGenerationException(String entityName, int attemptCount) {
        this.entityName = entityName;
        this.attemptCount = attemptCount;
    }
}
