package hu.kalmancheysandor.applications.dominions.servers.admin.service.game.scenario.exception;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record.RecordNotFoundByIdException;

public class GameScenarioNotFoundException extends RecordNotFoundByIdException {
    public GameScenarioNotFoundException(int id) {
        super(id);
    }
}
