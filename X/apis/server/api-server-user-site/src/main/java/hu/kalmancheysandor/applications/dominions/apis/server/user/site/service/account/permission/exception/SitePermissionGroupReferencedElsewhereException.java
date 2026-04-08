package hu.kalmancheysandor.applications.dominions.apis.server.user.site.service.account.permission.exception;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record.RecordReferencedElsewhereException;

public class SitePermissionGroupReferencedElsewhereException extends RecordReferencedElsewhereException {
    private String name;

    public SitePermissionGroupReferencedElsewhereException(int id, String name) {
        super(id);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
