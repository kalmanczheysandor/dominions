package hu.kalmancheysandor.applications.dominions.service.account.profile.exception;

import hu.kalmancheysandor.applications.dominions.service.exception.record.RecordDuplicateConstraintException;
import hu.kalmancheysandor.applications.dominions.service.exception.record.RecordDuplicateConstraintException;
import hu.kalmancheysandor.applications.dominions.service.exception.record.RecordDuplicateConstraintException;

public class ProfileIdentifierIsReservedException extends RecordDuplicateConstraintException {
    private String identifier;

    public ProfileIdentifierIsReservedException(String identifier) {
        super("identifier", identifier);
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }
}
