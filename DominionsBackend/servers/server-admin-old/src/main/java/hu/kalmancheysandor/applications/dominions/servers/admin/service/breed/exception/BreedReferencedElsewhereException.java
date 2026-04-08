package hu.kalmancheysandor.applications.dominions.service.breed.exception;

import hu.kalmancheysandor.applications.dominions.service.exception.record.RecordReferencedElsewhereException;
import hu.kalmancheysandor.applications.dominions.service.exception.record.RecordReferencedElsewhereException;
import hu.kalmancheysandor.applications.dominions.service.exception.record.RecordReferencedElsewhereException;

public class BreedReferencedElsewhereException extends RecordReferencedElsewhereException {
    private String name;

    public BreedReferencedElsewhereException(int id, String name) {
        super(id);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
