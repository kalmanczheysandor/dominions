package hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.exception.heuristic;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record.RecordReferencedElsewhereException;

public class HugoHeuristicReferencedElsewhereException extends RecordReferencedElsewhereException {
    private String name;

    public HugoHeuristicReferencedElsewhereException(int id, String name) {
        super(id);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
