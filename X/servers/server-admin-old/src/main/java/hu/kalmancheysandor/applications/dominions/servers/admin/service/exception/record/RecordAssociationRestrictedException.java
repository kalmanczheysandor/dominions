package hu.kalmancheysandor.applications.dominions.service.exception.record;

import hu.kalmancheysandor.applications.dominions.service.exception.CustomException;
import hu.kalmancheysandor.applications.dominions.service.exception.CustomException;
import hu.kalmancheysandor.applications.dominions.service.exception.CustomException;

public abstract class RecordAssociationRestrictedException extends CustomException {
    private int id;

    public RecordAssociationRestrictedException(int id) {
        super("RecordAssociationRestricted");
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "RecordAssociationRestrictedException{" +
            "id=" + id +
            '}';
    }
}
