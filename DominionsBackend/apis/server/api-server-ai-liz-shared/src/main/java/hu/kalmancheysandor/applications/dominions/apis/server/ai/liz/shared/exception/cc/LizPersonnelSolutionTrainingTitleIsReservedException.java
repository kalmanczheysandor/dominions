package hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.exception.cc;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record.RecordDuplicateConstraintException;

public class LizPersonnelSolutionTrainingTitleIsReservedException extends RecordDuplicateConstraintException {
    private String title;

    public LizPersonnelSolutionTrainingTitleIsReservedException(String title) {
        super("title", title);
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
