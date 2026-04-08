package hu.kalmancheysandor.applications.dominions.servers.site.service.account.settings.exception;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record.RecordAssociationRestrictedException;

public class SiteAccountSettingsAssociationRestrictedException extends RecordAssociationRestrictedException {
    private String identifier;

    public SiteAccountSettingsAssociationRestrictedException(int id, String name) {
        super(id);
        this.identifier = name;
    }

    public String getIdentifier() {
        return identifier;
    }
}
