package hu.kalmancheysandor.applications.dominions.apis.server.user.admin.service.account.permission.exception;

public class AdminPermissionGroupNotFoundByUuidException extends RuntimeException {
    private String uuid;

    public AdminPermissionGroupNotFoundByUuidException(String uuid) {
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
