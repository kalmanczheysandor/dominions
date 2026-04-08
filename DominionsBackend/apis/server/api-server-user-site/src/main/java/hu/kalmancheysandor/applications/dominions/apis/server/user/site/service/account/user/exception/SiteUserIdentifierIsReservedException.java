package hu.kalmancheysandor.applications.dominions.apis.server.user.site.service.account.user.exception;

import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record.RecordDuplicateConstraintException;

public class SiteUserIdentifierIsReservedException extends RecordDuplicateConstraintException {
    private String identifier;

    public SiteUserIdentifierIsReservedException(String identifier) {
        super("identifier", identifier);
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }
}
