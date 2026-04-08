package hu.kalmancheysandor.applications.dominions.service.breed.exception;

import hu.kalmancheysandor.applications.dominions.service.exception.record.RecordNotFoundByUuidException;
import hu.kalmancheysandor.applications.dominions.service.exception.record.RecordNotFoundByUuidException;
import hu.kalmancheysandor.applications.dominions.service.exception.record.RecordNotFoundByUuidException;

public class BreedNotFoundByUuidException extends RecordNotFoundByUuidException {
    public BreedNotFoundByUuidException(String uuid) {
        super(uuid);
    }
}
