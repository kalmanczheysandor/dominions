package hu.kalmancheysandor.applications.dominions.service.breed.exception;

import hu.kalmancheysandor.applications.dominions.service.exception.record.RecordNotFoundByIdException;
import hu.kalmancheysandor.applications.dominions.service.exception.record.RecordNotFoundByIdException;
import hu.kalmancheysandor.applications.dominions.service.exception.record.RecordNotFoundByIdException;

public class BreedNotFoundException extends RecordNotFoundByIdException {
    public BreedNotFoundException(int id) {
        super(id);
    }
}
