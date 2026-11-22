package hu.kalmancheysandor.applications.dominions.apis.server.ai.otto.shared.exception.evaluator;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record.RecordDuplicateConstraintException;

public class OttoEvaluatorCodeIsReservedException extends RecordDuplicateConstraintException {
    private String code;

    public OttoEvaluatorCodeIsReservedException(String code) {
        super("code", code);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
