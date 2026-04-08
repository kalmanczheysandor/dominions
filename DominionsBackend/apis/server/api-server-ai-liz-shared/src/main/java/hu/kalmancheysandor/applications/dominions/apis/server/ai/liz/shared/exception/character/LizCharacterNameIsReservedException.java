package hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.exception.character;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record.RecordDuplicateConstraintException;

public class LizCharacterNameIsReservedException extends RecordDuplicateConstraintException {
    private String name;

    public LizCharacterNameIsReservedException(String name) {
        super("name", name);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
