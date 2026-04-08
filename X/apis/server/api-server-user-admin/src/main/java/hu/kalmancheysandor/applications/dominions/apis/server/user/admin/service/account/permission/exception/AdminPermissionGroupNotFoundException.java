package hu.kalmancheysandor.applications.dominions.apis.server.user.admin.service.account.permission.exception;

public class AdminPermissionGroupNotFoundException extends RuntimeException {
    private int id;
    public AdminPermissionGroupNotFoundException(int id) {
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
