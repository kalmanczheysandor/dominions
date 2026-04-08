package hu.kalmancheysandor.applications.dominions.apis.server.user.admin.service.account.permission.exception;

public class AdminPermissionGroupForeignKeyViolationException extends RuntimeException {
    private int id;
    public AdminPermissionGroupForeignKeyViolationException(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }




}
