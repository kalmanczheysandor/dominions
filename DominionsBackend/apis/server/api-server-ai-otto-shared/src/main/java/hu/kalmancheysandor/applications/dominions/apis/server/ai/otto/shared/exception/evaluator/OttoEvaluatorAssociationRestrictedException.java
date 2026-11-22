package hu.kalmancheysandor.applications.dominions.apis.server.ai.otto.shared.exception.evaluator;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record.RecordAssociationRestrictedException;

public class OttoEvaluatorAssociationRestrictedException extends RecordAssociationRestrictedException {
    private String title;

    public OttoEvaluatorAssociationRestrictedException(int id, String title) {
        super(id);
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
