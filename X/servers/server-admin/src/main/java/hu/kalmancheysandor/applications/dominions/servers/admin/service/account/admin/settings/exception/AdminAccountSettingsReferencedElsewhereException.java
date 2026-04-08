package hu.kalmancheysandor.applications.dominions.servers.admin.service.account.admin.settings.exception;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record.RecordReferencedElsewhereException;

public class AdminAccountSettingsReferencedElsewhereException extends RecordReferencedElsewhereException {
    private String identifier;

    public AdminAccountSettingsReferencedElsewhereException(int id, String identifier) {
        super(id);
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }
}
