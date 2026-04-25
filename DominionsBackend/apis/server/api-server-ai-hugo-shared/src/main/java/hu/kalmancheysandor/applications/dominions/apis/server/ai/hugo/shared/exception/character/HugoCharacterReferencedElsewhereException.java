package hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.exception.character;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record.RecordReferencedElsewhereException;

public class HugoCharacterReferencedElsewhereException extends RecordReferencedElsewhereException {
    private String name;

    public HugoCharacterReferencedElsewhereException(int id, String name) {
        super(id);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
