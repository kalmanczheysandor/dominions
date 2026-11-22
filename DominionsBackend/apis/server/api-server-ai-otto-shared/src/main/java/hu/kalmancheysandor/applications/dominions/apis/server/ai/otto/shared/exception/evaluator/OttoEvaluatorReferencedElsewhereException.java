package hu.kalmancheysandor.applications.dominions.apis.server.ai.otto.shared.exception.evaluator;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record.RecordReferencedElsewhereException;

public class OttoEvaluatorReferencedElsewhereException extends RecordReferencedElsewhereException {
    private String name;

    public OttoEvaluatorReferencedElsewhereException(int id, String name) {
        super(id);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
