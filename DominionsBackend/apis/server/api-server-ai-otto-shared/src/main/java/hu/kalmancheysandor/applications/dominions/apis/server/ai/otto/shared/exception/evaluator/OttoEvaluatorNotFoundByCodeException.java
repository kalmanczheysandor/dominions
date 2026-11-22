package hu.kalmancheysandor.applications.dominions.apis.server.ai.otto.shared.exception.evaluator;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.CustomException;

public class OttoEvaluatorNotFoundByCodeException extends CustomException {
    private String code;

    public OttoEvaluatorNotFoundByCodeException(String code) {
        super("OttoHeuristicNotFoundByCodeException");
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
