package hu.kalmancheysandor.applications.dominions.service.account.permission.exception;

public class PermissionGroupNotFoundByUuidException extends RuntimeException {
    private String uuid;

    public PermissionGroupNotFoundByUuidException(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }

    @Override
    public String toString() {
        return "PermissionGroupNotFoundByUuidException{" +
            "uuid='" + uuid + '\'' +
            '}';
    }
}
