package hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.exception.heuristic;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record.RecordDuplicateConstraintException;

public class HugoHeuristicCodeIsReservedException extends RecordDuplicateConstraintException {
    private String code;

    public HugoHeuristicCodeIsReservedException(String code) {
        super("code", code);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
