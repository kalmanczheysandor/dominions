package hu.kalmancheysandor.applications.dominions.service.account.permission.exception;

import hu.kalmancheysandor.applications.dominions.service.exception.record.RecordAssociationRestrictedException;
import hu.kalmancheysandor.applications.dominions.service.exception.record.RecordAssociationRestrictedException;
import hu.kalmancheysandor.applications.dominions.service.exception.record.RecordAssociationRestrictedException;

public class PermissionGroupAssociationRestrictedException extends RecordAssociationRestrictedException {
    private String name;

    public PermissionGroupAssociationRestrictedException(int id, String name) {
        super(id);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
