package hu.kalmancheysandor.applications.dominions.servers.admin.service.account.admin.settings.exception;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record.RecordDuplicateConstraintException;

public class AdminAccountSettingsIdentifierIsReservedException extends RecordDuplicateConstraintException {
    private String identifier;

    public AdminAccountSettingsIdentifierIsReservedException(String identifier) {
        super("identifier", identifier);
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }
}
