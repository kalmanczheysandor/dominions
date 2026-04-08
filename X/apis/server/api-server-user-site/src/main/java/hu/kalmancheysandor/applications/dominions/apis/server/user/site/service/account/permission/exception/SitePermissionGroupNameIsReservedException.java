package hu.kalmancheysandor.applications.dominions.apis.server.user.site.service.account.permission.exception;

public class SitePermissionGroupNameIsReservedException extends RuntimeException {
    private String name;

    public SitePermissionGroupNameIsReservedException(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "PermissionGroupNameIsReservedException{" +
            "name='" + name + '\'' +
            '}';
    }
}
