package hu.kalmancheysandor.applications.dominions.servers.admin.service.game.scenario.exception;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record.RecordDuplicateConstraintException;

public class GameScenarioTitleIsReservedException extends RecordDuplicateConstraintException {
    private String title;

    public GameScenarioTitleIsReservedException(String title) {
        super("title", title);
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
