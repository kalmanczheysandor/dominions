package hu.kalmancheysandor.applications.dominions.apis.server.ai.otto.shared.exception.character;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record.RecordReferencedElsewhereException;

public class OttoCharacterReferencedElsewhereException extends RecordReferencedElsewhereException {
    private String name;

    public OttoCharacterReferencedElsewhereException(int id, String name) {
        super(id);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
