package hu.kalmancheysandor.applications.dominions.apis.server.game.common.exception.game.scenario;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record.RecordNotFoundByUuidException;

public class GameScenarioNotFoundByUuidException extends RecordNotFoundByUuidException {
    public GameScenarioNotFoundByUuidException(String uuid) {
        super(uuid);
    }
}
