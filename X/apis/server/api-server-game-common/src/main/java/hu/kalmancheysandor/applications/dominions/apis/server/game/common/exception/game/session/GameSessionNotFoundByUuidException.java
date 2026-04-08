package hu.kalmancheysandor.applications.dominions.apis.server.game.common.exception.game.session;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record.RecordNotFoundByUuidException;

public class GameSessionNotFoundByUuidException extends RecordNotFoundByUuidException {
    public GameSessionNotFoundByUuidException(String uuid) {
        super(uuid);
    }
}
