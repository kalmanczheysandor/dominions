package hu.kalmancheysandor.applications.dominions.service.breed.exception;

import hu.kalmancheysandor.applications.dominions.service.exception.record.RecordDuplicateConstraintException;
import hu.kalmancheysandor.applications.dominions.service.exception.record.RecordDuplicateConstraintException;
import hu.kalmancheysandor.applications.dominions.service.exception.record.RecordDuplicateConstraintException;

public class BreedNameIsReservedException extends RecordDuplicateConstraintException {
    private String name;

    public BreedNameIsReservedException(String name) {
        super("name", name);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
