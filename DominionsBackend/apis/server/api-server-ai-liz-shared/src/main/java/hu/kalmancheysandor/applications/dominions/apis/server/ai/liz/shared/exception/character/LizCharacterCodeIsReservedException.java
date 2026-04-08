package hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.exception.character;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record.RecordDuplicateConstraintException;

public class LizCharacterCodeIsReservedException extends RecordDuplicateConstraintException {
    private String code;

    public LizCharacterCodeIsReservedException(String code) {
        super("code", code);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
