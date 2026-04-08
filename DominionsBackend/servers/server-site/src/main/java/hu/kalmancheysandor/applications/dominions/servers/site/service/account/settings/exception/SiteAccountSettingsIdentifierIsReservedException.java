package hu.kalmancheysandor.applications.dominions.servers.site.service.account.settings.exception;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record.RecordDuplicateConstraintException;

public class SiteAccountSettingsIdentifierIsReservedException extends RecordDuplicateConstraintException {
    private String identifier;

    public SiteAccountSettingsIdentifierIsReservedException(String identifier) {
        super("identifier", identifier);
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }
}
