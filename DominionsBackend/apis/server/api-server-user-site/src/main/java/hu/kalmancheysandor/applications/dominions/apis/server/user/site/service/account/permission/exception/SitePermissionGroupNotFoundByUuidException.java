package hu.kalmancheysandor.applications.dominions.apis.server.user.site.service.account.permission.exception;

public class SitePermissionGroupNotFoundByUuidException extends RuntimeException {
    private String uuid;

    public SitePermissionGroupNotFoundByUuidException(String uuid) {
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
