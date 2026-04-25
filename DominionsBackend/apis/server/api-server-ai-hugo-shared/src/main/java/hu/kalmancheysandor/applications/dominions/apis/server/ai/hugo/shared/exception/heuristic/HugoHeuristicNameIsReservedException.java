package hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.exception.heuristic;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record.RecordDuplicateConstraintException;

public class HugoHeuristicNameIsReservedException extends RecordDuplicateConstraintException {
    private String name;

    public HugoHeuristicNameIsReservedException(String name) {
        super("name", name);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
