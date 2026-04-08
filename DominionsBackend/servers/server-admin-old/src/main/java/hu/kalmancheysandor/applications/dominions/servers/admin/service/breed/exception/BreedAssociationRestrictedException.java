package hu.kalmancheysandor.applications.dominions.service.breed.exception;

import hu.kalmancheysandor.applications.dominions.service.exception.record.RecordAssociationRestrictedException;
import hu.kalmancheysandor.applications.dominions.service.exception.record.RecordAssociationRestrictedException;
import hu.kalmancheysandor.applications.dominions.service.exception.record.RecordAssociationRestrictedException;

public class BreedAssociationRestrictedException extends RecordAssociationRestrictedException {
    private String name;

    public BreedAssociationRestrictedException(int id, String name) {
        super(id);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
