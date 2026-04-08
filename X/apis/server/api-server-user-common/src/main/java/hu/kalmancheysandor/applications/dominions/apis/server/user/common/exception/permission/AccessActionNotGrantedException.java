package hu.kalmancheysandor.applications.dominions.apis.server.user.common.exception.permission;

public class AccessActionNotGrantedException extends ActionNotGrantedException {

    public AccessActionNotGrantedException(String resource) {
        super(resource, "ACCESS");
    }
}
