package hu.kalmancheysandor.applications.dominions.service.account.permission.exception;

public class PermissionGroupNameIsReservedException extends RuntimeException {
    private String name;

    public PermissionGroupNameIsReservedException(String name) {
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
