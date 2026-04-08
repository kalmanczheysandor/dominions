package hu.kalmancheysandor.applications.dominions.service.exception.permission;

public class EditActionNotGrantedException extends ActionNotGrantedException {

    public EditActionNotGrantedException(String resource) {
        super(resource,"EDIT");
    }
}
