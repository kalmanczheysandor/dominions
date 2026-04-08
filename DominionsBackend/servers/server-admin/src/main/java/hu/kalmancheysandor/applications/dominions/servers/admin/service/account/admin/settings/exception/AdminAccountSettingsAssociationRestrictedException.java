package hu.kalmancheysandor.applications.dominions.servers.admin.service.account.admin.settings.exception;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record.RecordAssociationRestrictedException;

public class AdminAccountSettingsAssociationRestrictedException extends RecordAssociationRestrictedException {
    private String identifier;

    public AdminAccountSettingsAssociationRestrictedException(int id, String name) {
        super(id);
        this.identifier = name;
    }

    public String getIdentifier() {
        return identifier;
    }
}
