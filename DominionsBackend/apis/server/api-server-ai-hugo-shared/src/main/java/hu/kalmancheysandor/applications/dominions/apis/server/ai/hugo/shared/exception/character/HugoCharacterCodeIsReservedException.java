package hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.exception.character;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record.RecordDuplicateConstraintException;

public class HugoCharacterCodeIsReservedException extends RecordDuplicateConstraintException {
    private String code;

    public HugoCharacterCodeIsReservedException(String code) {
        super("code", code);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
