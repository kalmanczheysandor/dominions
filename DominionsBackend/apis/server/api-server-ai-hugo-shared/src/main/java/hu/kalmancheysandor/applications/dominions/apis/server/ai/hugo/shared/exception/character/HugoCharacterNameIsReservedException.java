package hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.exception.character;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record.RecordDuplicateConstraintException;

public class HugoCharacterNameIsReservedException extends RecordDuplicateConstraintException {
    private String name;

    public HugoCharacterNameIsReservedException(String name) {
        super("name", name);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
