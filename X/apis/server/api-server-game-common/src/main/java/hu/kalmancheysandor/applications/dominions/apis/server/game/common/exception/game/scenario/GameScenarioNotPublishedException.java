package hu.kalmancheysandor.applications.dominions.apis.server.game.common.exception.game.scenario;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.CustomException;

public class GameScenarioNotPublishedException extends CustomException {
    private String uuid;

    public GameScenarioNotPublishedException(String uuid) {
        super("GameScenarioNotPublished");
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }
}
