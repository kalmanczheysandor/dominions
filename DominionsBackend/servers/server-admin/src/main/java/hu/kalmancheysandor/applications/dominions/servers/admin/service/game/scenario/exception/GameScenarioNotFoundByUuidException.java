package hu.kalmancheysandor.applications.dominions.servers.admin.service.game.scenario.exception;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record.RecordNotFoundByUuidException;

public class GameScenarioNotFoundByUuidException extends RecordNotFoundByUuidException {
    public GameScenarioNotFoundByUuidException(String uuid) {
        super(uuid);
    }
}
