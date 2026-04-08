package hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record;

import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.CustomException;

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
