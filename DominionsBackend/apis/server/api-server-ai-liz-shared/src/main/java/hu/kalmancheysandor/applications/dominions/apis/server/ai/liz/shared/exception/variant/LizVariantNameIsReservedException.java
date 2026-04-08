package hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.exception.variant;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record.RecordDuplicateConstraintException;

public class LizVariantNameIsReservedException extends RecordDuplicateConstraintException {
    private String name;

    public LizVariantNameIsReservedException(String name) {
        super("name", name);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
