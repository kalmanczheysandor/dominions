package hu.kalmancheysandor.applications.dominions.apis.server.user.common.exception.permission;

public class DeleteActionNotGrantedException extends ActionNotGrantedException {

    public DeleteActionNotGrantedException(String resource) {
        super(resource,"DELETE");
    }
}
