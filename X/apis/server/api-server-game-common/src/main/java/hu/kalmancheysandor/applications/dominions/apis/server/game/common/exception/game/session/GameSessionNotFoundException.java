package hu.kalmancheysandor.applications.dominions.apis.server.game.common.exception.game.session;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record.RecordNotFoundByIdException;

public class GameSessionNotFoundException extends RecordNotFoundByIdException {
    public GameSessionNotFoundException(int id) {
        super(id);
    }
}
