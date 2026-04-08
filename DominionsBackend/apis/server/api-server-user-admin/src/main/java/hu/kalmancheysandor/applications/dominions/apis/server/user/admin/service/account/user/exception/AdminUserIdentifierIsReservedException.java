package hu.kalmancheysandor.applications.dominions.apis.server.user.admin.service.account.user.exception;

import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record.RecordDuplicateConstraintException;

public class AdminUserIdentifierIsReservedException extends RecordDuplicateConstraintException {
    private String identifier;

    public AdminUserIdentifierIsReservedException(String identifier) {
        super("identifier", identifier);
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }
}
