package hu.kalmancheysandor.applications.dominions.service.account.user.exception;

import hu.kalmancheysandor.applications.dominions.service.exception.record.RecordAssociationRestrictedException;
import hu.kalmancheysandor.applications.dominions.service.exception.record.RecordAssociationRestrictedException;
import hu.kalmancheysandor.applications.dominions.service.exception.record.RecordAssociationRestrictedException;

public class UserAssociationRestrictedException extends RecordAssociationRestrictedException {
    private String identifier;

    public UserAssociationRestrictedException(int id, String name) {
        super(id);
        this.identifier = name;
    }

    public String getIdentifier() {
        return identifier;
    }
}
