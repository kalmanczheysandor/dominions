package hu.kalmancheysandor.applications.dominions.apis.server.ai.otto.shared.exception.character;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record.RecordDuplicateConstraintException;

public class OttoCharacterNameIsReservedException extends RecordDuplicateConstraintException {
    private String name;

    public OttoCharacterNameIsReservedException(String name) {
        super("name", name);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
