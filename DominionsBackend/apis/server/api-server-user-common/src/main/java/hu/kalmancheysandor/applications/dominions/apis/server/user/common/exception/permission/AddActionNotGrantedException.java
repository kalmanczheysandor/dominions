package hu.kalmancheysandor.applications.dominions.apis.server.user.common.exception.permission;

public class AddActionNotGrantedException extends ActionNotGrantedException {

    public AddActionNotGrantedException(String resource) {
        super(resource,"ADD");
    }
}
