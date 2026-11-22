package hu.kalmancheysandor.applications.dominions.apis.server.ai.otto.shared.exception.evaluator.evaluator;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.CustomException;

public class OttoUnexpectedOperatorScoreEvaluatorTypeCodeException extends CustomException {
   private String typeCode;

    public OttoUnexpectedOperatorScoreEvaluatorTypeCodeException(String typeCode) {
        super("OttoUnexpectedOperatorScoreEvaluatorTypeCodeException");
        this.typeCode = typeCode;
    }

    public String getTypeCode() {
        return typeCode;
    }
}
