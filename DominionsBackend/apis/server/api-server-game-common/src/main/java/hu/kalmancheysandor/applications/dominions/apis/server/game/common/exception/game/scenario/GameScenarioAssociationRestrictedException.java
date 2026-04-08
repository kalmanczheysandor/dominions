package hu.kalmancheysandor.applications.dominions.servers.admin.service.game.scenario.exception;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record.RecordAssociationRestrictedException;

public class GameScenarioAssociationRestrictedException extends RecordAssociationRestrictedException {
    private String title;

    public GameScenarioAssociationRestrictedException(int id, String title) {
        super(id);
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
