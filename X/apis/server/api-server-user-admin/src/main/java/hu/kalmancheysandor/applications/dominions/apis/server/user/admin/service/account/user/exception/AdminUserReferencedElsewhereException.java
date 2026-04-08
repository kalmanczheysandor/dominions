package hu.kalmancheysandor.applications.dominions.apis.server.user.admin.service.account.user.exception;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record.RecordReferencedElsewhereException;

public class AdminUserReferencedElsewhereException extends RecordReferencedElsewhereException {
    private String identifier;

    public AdminUserReferencedElsewhereException(int id, String identifier) {
        super(id);
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }
}
