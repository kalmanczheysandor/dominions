package hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.exception.cc;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record.RecordReferencedElsewhereException;

public class LizPersonnelSolutionTrainingReferencedElsewhereException extends RecordReferencedElsewhereException {
    private String title;

    public LizPersonnelSolutionTrainingReferencedElsewhereException(int id, String title) {
        super(id);
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
