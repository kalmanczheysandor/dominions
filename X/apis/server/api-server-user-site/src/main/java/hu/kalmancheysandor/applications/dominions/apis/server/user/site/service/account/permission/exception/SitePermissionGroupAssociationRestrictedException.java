package hu.kalmancheysandor.applications.dominions.apis.server.user.site.service.account.permission.exception;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record.RecordAssociationRestrictedException;

public class SitePermissionGroupAssociationRestrictedException extends RecordAssociationRestrictedException {
    private String name;

    public SitePermissionGroupAssociationRestrictedException(int id, String name) {
        super(id);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
