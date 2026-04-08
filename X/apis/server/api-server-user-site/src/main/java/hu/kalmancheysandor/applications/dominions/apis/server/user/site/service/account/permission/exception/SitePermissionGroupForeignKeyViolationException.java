package hu.kalmancheysandor.applications.dominions.apis.server.user.site.service.account.permission.exception;

public class SitePermissionGroupForeignKeyViolationException extends RuntimeException {
    private int id;
    public SitePermissionGroupForeignKeyViolationException(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }




}
