package hu.kalmancheysandor.applications.dominions.apis.server.ai.otto.shared.exception.evaluator;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record.RecordNotFoundByIdException;

public class OttoEvaluatorNotFoundException extends RecordNotFoundByIdException {
    public OttoEvaluatorNotFoundException(int id) {
        super(id);
    }
}
