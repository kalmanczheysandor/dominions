package hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.exception.variant;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record.RecordReferencedElsewhereException;

public class HugoVariantReferencedElsewhereException extends RecordReferencedElsewhereException {
    private String name;

    public HugoVariantReferencedElsewhereException(int id, String name) {
        super(id);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
