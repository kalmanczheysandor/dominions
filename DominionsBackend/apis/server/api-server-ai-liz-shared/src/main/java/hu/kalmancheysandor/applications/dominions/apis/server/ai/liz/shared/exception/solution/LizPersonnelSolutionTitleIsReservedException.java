package hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.exception.solution;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record.RecordDuplicateConstraintException;

public class LizPersonnelSolutionTitleIsReservedException extends RecordDuplicateConstraintException {
    private String title;

    public LizPersonnelSolutionTitleIsReservedException(String title) {
        super("title", title);
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
