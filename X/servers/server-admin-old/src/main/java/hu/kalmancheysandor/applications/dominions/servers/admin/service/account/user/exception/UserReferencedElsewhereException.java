package hu.kalmancheysandor.applications.dominions.service.account.user.exception;

import hu.kalmancheysandor.applications.dominions.service.exception.record.RecordReferencedElsewhereException;
import hu.kalmancheysandor.applications.dominions.service.exception.record.RecordReferencedElsewhereException;
import hu.kalmancheysandor.applications.dominions.service.exception.record.RecordReferencedElsewhereException;

public class UserReferencedElsewhereException extends RecordReferencedElsewhereException {
    private String identifier;

    public UserReferencedElsewhereException(int id, String identifier) {
        super(id);
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }
}
