package hu.kalmancheysandor.applications.dominions.apis.server.game.common.exception.game.scenario;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record.RecordNotFoundByIdException;

public class GameScenarioNotFoundException extends RecordNotFoundByIdException {
    public GameScenarioNotFoundException(int id) {
        super(id);
    }
}
