package hu.kalmancheysandor.applications.dominions.service.exception.permission;

public class AccessActionNotGrantedException extends ActionNotGrantedException {

    public AccessActionNotGrantedException(String resource) {
        super(resource, "ACCESS");
    }
}
