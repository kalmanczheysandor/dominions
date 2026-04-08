package hu.kalmancheysandor.applications.dominions.service.account.profile.exception;

import hu.kalmancheysandor.applications.dominions.service.exception.record.RecordReferencedElsewhereException;
import hu.kalmancheysandor.applications.dominions.service.exception.record.RecordReferencedElsewhereException;
import hu.kalmancheysandor.applications.dominions.service.exception.record.RecordReferencedElsewhereException;

public class ProfileReferencedElsewhereException extends RecordReferencedElsewhereException {
    private String identifier;

    public ProfileReferencedElsewhereException(int id, String identifier) {
        super(id);
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }
}
