package hu.kalmancheysandor.applications.dominions.service.account.permission.exception;

public class PermissionGroupNotFoundException extends RuntimeException {
    private int id;
    public PermissionGroupNotFoundException(int id) {
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
