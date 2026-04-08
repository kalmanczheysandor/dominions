package hu.kalmancheysandor.applications.dominions.apis.server.game.common.exception.game.session;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record.RecordReferencedElsewhereException;

public class GameSessionReferencedElsewhereException extends RecordReferencedElsewhereException {
    private String name;

    public GameSessionReferencedElsewhereException(int id, String name) {
        super(id);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
