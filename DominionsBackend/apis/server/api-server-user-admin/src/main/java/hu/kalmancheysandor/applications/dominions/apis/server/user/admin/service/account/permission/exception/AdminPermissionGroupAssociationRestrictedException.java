package hu.kalmancheysandor.applications.dominions.apis.server.user.admin.service.account.permission.exception;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record.RecordAssociationRestrictedException;

public class AdminPermissionGroupAssociationRestrictedException extends RecordAssociationRestrictedException {
    private String name;

    public AdminPermissionGroupAssociationRestrictedException(int id, String name) {
        super(id);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
