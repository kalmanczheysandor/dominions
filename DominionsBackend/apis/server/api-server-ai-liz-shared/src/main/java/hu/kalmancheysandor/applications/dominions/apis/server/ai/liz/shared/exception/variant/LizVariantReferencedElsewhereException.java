package hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.exception.variant;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record.RecordReferencedElsewhereException;

public class LizVariantReferencedElsewhereException extends RecordReferencedElsewhereException {
    private String name;

    public LizVariantReferencedElsewhereException(int id, String name) {
        super(id);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
