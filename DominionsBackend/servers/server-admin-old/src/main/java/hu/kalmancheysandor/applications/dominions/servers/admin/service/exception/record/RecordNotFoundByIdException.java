package hu.kalmancheysandor.applications.dominions.service.exception.record;

import hu.kalmancheysandor.applications.dominions.service.exception.CustomException;
import hu.kalmancheysandor.applications.dominions.service.exception.CustomException;
import hu.kalmancheysandor.applications.dominions.service.exception.CustomException;

public abstract class RecordNotFoundByIdException extends CustomException {
    private int id;

    public RecordNotFoundByIdException(int id) {
        super("RecordNotFound");
        this.id = id;
    }

    public final int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "RecordNotFoundByIdException{" +
            "id=" + id +
            '}';
    }
}
