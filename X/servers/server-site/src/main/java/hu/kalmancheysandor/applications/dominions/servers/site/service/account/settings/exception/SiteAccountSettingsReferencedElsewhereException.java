package hu.kalmancheysandor.applications.dominions.servers.site.service.account.settings.exception;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record.RecordReferencedElsewhereException;

public class SiteAccountSettingsReferencedElsewhereException extends RecordReferencedElsewhereException {
    private String identifier;

    public SiteAccountSettingsReferencedElsewhereException(int id, String identifier) {
        super(id);
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }
}
