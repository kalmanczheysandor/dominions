package hu.kalmancheysandor.applications.dominions.apis.server.user.site.service.account.user.exception;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record.RecordReferencedElsewhereException;

public class SiteUserReferencedElsewhereException extends RecordReferencedElsewhereException {
    private String identifier;

    public SiteUserReferencedElsewhereException(int id, String identifier) {
        super(id);
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }
}
