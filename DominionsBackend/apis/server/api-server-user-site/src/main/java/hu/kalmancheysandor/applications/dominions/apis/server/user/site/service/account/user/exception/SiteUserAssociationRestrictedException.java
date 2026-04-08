package hu.kalmancheysandor.applications.dominions.apis.server.user.site.service.account.user.exception;

import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record.RecordAssociationRestrictedException;

public class SiteUserAssociationRestrictedException extends RecordAssociationRestrictedException {
    private String identifier;

    public SiteUserAssociationRestrictedException(int id, String identifier) {
        super(id);
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }
}
