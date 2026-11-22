package hu.kalmancheysandor.applications.dominions.apis.server.ai.otto.shared.exception.variant;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record.RecordReferencedElsewhereException;

public class OttoVariantReferencedElsewhereException extends RecordReferencedElsewhereException {
    private String name;

    public OttoVariantReferencedElsewhereException(int id, String name) {
        super(id);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
