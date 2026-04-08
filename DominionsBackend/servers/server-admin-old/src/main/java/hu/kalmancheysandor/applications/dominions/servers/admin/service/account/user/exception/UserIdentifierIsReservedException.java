package hu.kalmancheysandor.applications.dominions.service.account.user.exception;

import hu.kalmancheysandor.applications.dominions.service.exception.record.RecordDuplicateConstraintException;
import hu.kalmancheysandor.applications.dominions.service.exception.record.RecordDuplicateConstraintException;
import hu.kalmancheysandor.applications.dominions.service.exception.record.RecordDuplicateConstraintException;

public class UserIdentifierIsReservedException extends RecordDuplicateConstraintException {
    private String identifier;

    public UserIdentifierIsReservedException(String identifier) {
        super("identifier", identifier);
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }
}
