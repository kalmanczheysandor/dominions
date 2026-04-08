package hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.exception.concept;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record.RecordReferencedElsewhereException;

public class LizNeuralConceptReferencedElsewhereException extends RecordReferencedElsewhereException {
    private String name;

    public LizNeuralConceptReferencedElsewhereException(int id, String name) {
        super(id);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
