package hu.kalmancheysandor.applications.dominions.service.exception;

public abstract class CustomException extends RuntimeException {
    private String group;

    public CustomException(String group) {
        this.group = group;
    }

    public final String getGroup() {
        return group;
    }
}
