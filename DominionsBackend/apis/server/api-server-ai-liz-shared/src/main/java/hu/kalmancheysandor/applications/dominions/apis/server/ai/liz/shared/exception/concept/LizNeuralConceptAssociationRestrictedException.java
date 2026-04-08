package hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.exception.concept;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record.RecordAssociationRestrictedException;

public class LizNeuralConceptAssociationRestrictedException extends RecordAssociationRestrictedException {
    private String title;

    public LizNeuralConceptAssociationRestrictedException(int id, String title) {
        super(id);
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
