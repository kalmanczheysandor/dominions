package hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.exception.variant;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record.RecordDuplicateConstraintException;

public class HugoVariantNameIsReservedException extends RecordDuplicateConstraintException {
    private String name;

    public HugoVariantNameIsReservedException(String name) {
        super("name", name);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
