package hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.exception.cc;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record.RecordAssociationRestrictedException;

public class LizPersonnelSolutionTrainingAssociationRestrictedException extends RecordAssociationRestrictedException {
    private String title;

    public LizPersonnelSolutionTrainingAssociationRestrictedException(int id, String title) {
        super(id);
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
