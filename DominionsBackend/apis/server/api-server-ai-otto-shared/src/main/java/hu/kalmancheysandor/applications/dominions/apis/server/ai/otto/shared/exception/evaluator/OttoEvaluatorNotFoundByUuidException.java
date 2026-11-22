package hu.kalmancheysandor.applications.dominions.apis.server.ai.otto.shared.exception.evaluator;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record.RecordNotFoundByUuidException;

public class OttoEvaluatorNotFoundByUuidException extends RecordNotFoundByUuidException {
    public OttoEvaluatorNotFoundByUuidException(String uuid) {
        super(uuid);
    }
}
