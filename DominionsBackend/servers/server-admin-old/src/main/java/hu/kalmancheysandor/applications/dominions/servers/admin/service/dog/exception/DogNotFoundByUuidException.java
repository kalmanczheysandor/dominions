package hu.kalmancheysandor.applications.dominions.service.dog.exception;

import hu.kalmancheysandor.applications.dominions.service.exception.record.RecordNotFoundByUuidException;
import hu.kalmancheysandor.applications.dominions.service.exception.record.RecordNotFoundByUuidException;
import hu.kalmancheysandor.applications.dominions.service.exception.record.RecordNotFoundByUuidException;

public class DogNotFoundByUuidException extends RecordNotFoundByUuidException {
    public DogNotFoundByUuidException(String uuid) {
        super(uuid);
    }
}
