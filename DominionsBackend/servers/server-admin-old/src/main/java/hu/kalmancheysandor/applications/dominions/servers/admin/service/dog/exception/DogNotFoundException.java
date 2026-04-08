package hu.kalmancheysandor.applications.dominions.service.dog.exception;

import hu.kalmancheysandor.applications.dominions.service.exception.record.RecordNotFoundByIdException;
import hu.kalmancheysandor.applications.dominions.service.exception.record.RecordNotFoundByIdException;
import hu.kalmancheysandor.applications.dominions.service.exception.record.RecordNotFoundByIdException;

public class DogNotFoundException extends RecordNotFoundByIdException {
    public DogNotFoundException(int id) {
        super(id);
    }
}
