package hu.kalmancheysandor.applications.dominions.apis.server.user.admin.service.account.user.exception;

import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record.RecordAssociationRestrictedException;

public class AdminUserAssociationRestrictedException extends RecordAssociationRestrictedException {
    private String identifier;

    public AdminUserAssociationRestrictedException(int id, String name) {
        super(id);
        this.identifier = name;
    }

    public String getIdentifier() {
        return identifier;
    }
}
