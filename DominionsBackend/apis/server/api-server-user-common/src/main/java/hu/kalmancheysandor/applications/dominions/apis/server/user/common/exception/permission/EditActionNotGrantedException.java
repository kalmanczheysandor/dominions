package hu.kalmancheysandor.applications.dominions.apis.server.user.common.exception.permission;

public class EditActionNotGrantedException extends ActionNotGrantedException {

    public EditActionNotGrantedException(String resource) {
        super(resource,"EDIT");
    }
}
