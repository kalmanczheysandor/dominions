package hu.kalmancheysandor.applications.dominions.service.account.profile.exception;

import hu.kalmancheysandor.applications.dominions.service.exception.record.RecordAssociationRestrictedException;
import hu.kalmancheysandor.applications.dominions.service.exception.record.RecordAssociationRestrictedException;
import hu.kalmancheysandor.applications.dominions.service.exception.record.RecordAssociationRestrictedException;

public class ProfileAssociationRestrictedException extends RecordAssociationRestrictedException {
    private String identifier;

    public ProfileAssociationRestrictedException(int id, String name) {
        super(id);
        this.identifier = name;
    }

    public String getIdentifier() {
        return identifier;
    }
}
