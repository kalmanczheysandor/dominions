package hu.kalmancheysandor.applications.dominions.service.account.permission.exception;

public class PermissionGroupForeignKeyViolationException extends RuntimeException {
    private int id;
    public PermissionGroupForeignKeyViolationException(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }




}
