package hu.kalmancheysandor.applications.dominions.service.site.exception;

import hu.kalmancheysandor.applications.dominions.service.exception.record.RecordDuplicateConstraintException;
import hu.kalmancheysandor.applications.dominions.service.exception.record.RecordDuplicateConstraintException;
import hu.kalmancheysandor.applications.dominions.service.exception.record.RecordDuplicateConstraintException;

public class SiteNameIsReservedException extends RecordDuplicateConstraintException {
    private String name;

    public SiteNameIsReservedException(String name) {
        super("name", name);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
