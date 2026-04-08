package hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.exception.solution;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record.RecordReferencedElsewhereException;

public class LizPersonnelSolutionReferencedElsewhereException extends RecordReferencedElsewhereException {
    private String title;

    public LizPersonnelSolutionReferencedElsewhereException(int id, String title) {
        super(id);
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
