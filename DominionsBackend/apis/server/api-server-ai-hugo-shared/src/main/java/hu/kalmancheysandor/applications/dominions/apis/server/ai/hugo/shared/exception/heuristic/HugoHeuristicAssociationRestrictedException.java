package hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.exception.heuristic;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record.RecordAssociationRestrictedException;

public class HugoHeuristicAssociationRestrictedException extends RecordAssociationRestrictedException {
    private String title;

    public HugoHeuristicAssociationRestrictedException(int id, String title) {
        super(id);
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
