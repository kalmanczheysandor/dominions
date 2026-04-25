package hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.exception.heuristic.evaluator;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.CustomException;

public class HugoUnexpectedHeuristicEvaluatorTypeCodeException extends CustomException {
   private String typeCode;

    public HugoUnexpectedHeuristicEvaluatorTypeCodeException(String typeCode) {
        super("HugoUnexpectedHeuristicEvaluatorTypeCodeException");
        this.typeCode = typeCode;
    }

    public String getTypeCode() {
        return typeCode;
    }
}
