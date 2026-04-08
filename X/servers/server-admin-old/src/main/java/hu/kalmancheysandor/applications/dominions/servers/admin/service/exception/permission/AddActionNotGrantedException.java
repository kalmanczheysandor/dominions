package hu.kalmancheysandor.applications.dominions.service.exception.permission;

public class AddActionNotGrantedException extends ActionNotGrantedException {

    public AddActionNotGrantedException(String resource) {
        super(resource,"ADD");
    }
}
