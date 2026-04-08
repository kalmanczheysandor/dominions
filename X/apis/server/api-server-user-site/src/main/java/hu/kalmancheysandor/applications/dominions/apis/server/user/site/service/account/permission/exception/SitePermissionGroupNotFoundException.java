package hu.kalmancheysandor.applications.dominions.apis.server.user.site.service.account.permission.exception;

public class SitePermissionGroupNotFoundException extends RuntimeException {
    private int id;
    public SitePermissionGroupNotFoundException(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "PermissionGroupNotFoundException{" +
            "id=" + id +
            '}';
    }
}
