package hu.kalmancheysandor.applications.dominions.servers.admin.service.game.scenario.exception;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record.RecordReferencedElsewhereException;

public class GameScenarioReferencedElsewhereException extends RecordReferencedElsewhereException {
    private String title;

    public GameScenarioReferencedElsewhereException(int id, String title) {
        super(id);
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
