package hu.kalmancheysandor.applications.dominions.apis.server.user.admin.service.account.permission.exception;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record.RecordReferencedElsewhereException;

public class AdminPermissionGroupReferencedElsewhereException extends RecordReferencedElsewhereException {
    private String name;

    public AdminPermissionGroupReferencedElsewhereException(int id, String name) {
        super(id);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
