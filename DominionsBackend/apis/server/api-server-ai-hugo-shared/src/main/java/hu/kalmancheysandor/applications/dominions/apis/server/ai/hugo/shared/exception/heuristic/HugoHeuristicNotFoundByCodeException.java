package hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.exception.heuristic;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.CustomException;
import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record.RecordNotFoundByUuidException;

public class HugoHeuristicNotFoundByCodeException extends CustomException {
   private String code;

    public HugoHeuristicNotFoundByCodeException( String code) {
        super("HugoHeuristicNotFoundByCodeException");
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
