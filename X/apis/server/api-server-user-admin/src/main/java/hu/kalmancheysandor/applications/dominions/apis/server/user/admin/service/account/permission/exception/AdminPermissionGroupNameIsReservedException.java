package hu.kalmancheysandor.applications.dominions.apis.server.user.admin.service.account.permission.exception;

public class AdminPermissionGroupNameIsReservedException extends RuntimeException {
    private String name;

    public AdminPermissionGroupNameIsReservedException(String name) {
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
