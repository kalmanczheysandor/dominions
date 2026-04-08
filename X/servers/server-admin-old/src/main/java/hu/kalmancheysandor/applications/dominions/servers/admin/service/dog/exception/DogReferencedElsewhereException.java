package hu.kalmancheysandor.applications.dominions.service.dog.exception;

import hu.kalmancheysandor.applications.dominions.service.exception.record.RecordReferencedElsewhereException;
import hu.kalmancheysandor.applications.dominions.service.exception.record.RecordReferencedElsewhereException;
import hu.kalmancheysandor.applications.dominions.service.exception.record.RecordReferencedElsewhereException;

public class DogReferencedElsewhereException extends RecordReferencedElsewhereException {
    private String name;

    public DogReferencedElsewhereException(int id, String name) {
        super(id);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
