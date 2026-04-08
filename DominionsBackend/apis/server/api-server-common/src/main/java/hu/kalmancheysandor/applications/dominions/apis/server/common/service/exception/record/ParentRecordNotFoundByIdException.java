package hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.CustomException;

public abstract class ParentRecordNotFoundByIdException extends CustomException {
    private int id;

    public ParentRecordNotFoundByIdException(int id) {
        super("ParentRecordNotFound");
        this.id = id;
    }

    public final int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "ParentRecordNotFoundByIdException{" +
            "id=" + id +
            '}';
    }
}
