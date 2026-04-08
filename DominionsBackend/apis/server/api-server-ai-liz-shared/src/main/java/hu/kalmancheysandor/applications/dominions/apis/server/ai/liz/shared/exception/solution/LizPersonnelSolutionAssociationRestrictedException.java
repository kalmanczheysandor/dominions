package hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.exception.solution;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record.RecordAssociationRestrictedException;

public class LizPersonnelSolutionAssociationRestrictedException extends RecordAssociationRestrictedException {
    private String title;

    public LizPersonnelSolutionAssociationRestrictedException(int id, String title) {
        super(id);
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
