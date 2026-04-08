package hu.kalmancheysandor.applications.dominions.apis.server.game.common.exception.game.session;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record.RecordAssociationRestrictedException;

public class GameSessionAssociationRestrictedException extends RecordAssociationRestrictedException {
    private String name;

    public GameSessionAssociationRestrictedException(int id, String name) {
        super(id);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
