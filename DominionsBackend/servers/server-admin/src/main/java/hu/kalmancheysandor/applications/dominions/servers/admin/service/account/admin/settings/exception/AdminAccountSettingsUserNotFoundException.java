package hu.kalmancheysandor.applications.dominions.servers.admin.service.account.admin.settings.exception;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record.RecordNotFoundByIdException;

public class AdminAccountSettingsUserNotFoundException extends RecordNotFoundByIdException {
    public AdminAccountSettingsUserNotFoundException(int id) {
        super(id);
    }
}
