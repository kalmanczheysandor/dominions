package hu.kalmancheysandor.applications.dominions.apis.server.ai.otto.shared.exception.character;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record.RecordDuplicateConstraintException;

public class OttoCharacterCodeIsReservedException extends RecordDuplicateConstraintException {
    private String code;

    public OttoCharacterCodeIsReservedException(String code) {
        super("code", code);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
