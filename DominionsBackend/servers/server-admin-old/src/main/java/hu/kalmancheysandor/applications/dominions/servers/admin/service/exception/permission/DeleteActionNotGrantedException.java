package hu.kalmancheysandor.applications.dominions.service.exception.permission;

public class DeleteActionNotGrantedException extends ActionNotGrantedException {

    public DeleteActionNotGrantedException(String resource) {
        super(resource,"DELETE");
    }
}
