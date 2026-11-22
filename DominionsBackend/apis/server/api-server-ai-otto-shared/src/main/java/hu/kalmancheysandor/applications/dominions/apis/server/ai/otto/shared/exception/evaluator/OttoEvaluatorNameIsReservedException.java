package hu.kalmancheysandor.applications.dominions.apis.server.ai.otto.shared.exception.evaluator;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record.RecordDuplicateConstraintException;

public class OttoEvaluatorNameIsReservedException extends RecordDuplicateConstraintException {
    private String name;

    public OttoEvaluatorNameIsReservedException(String name) {
        super("name", name);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
