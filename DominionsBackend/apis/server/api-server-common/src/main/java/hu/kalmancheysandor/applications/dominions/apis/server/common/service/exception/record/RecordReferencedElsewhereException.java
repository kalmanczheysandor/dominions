package hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.CustomException;

public abstract class RecordReferencedElsewhereException extends CustomException {
    private int id;

    public RecordReferencedElsewhereException(int id) {
        super("RecordStillReferenced");
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "RecordStillReferencedException{" +
            "id=" + getId() +
            '}';
    }
}
