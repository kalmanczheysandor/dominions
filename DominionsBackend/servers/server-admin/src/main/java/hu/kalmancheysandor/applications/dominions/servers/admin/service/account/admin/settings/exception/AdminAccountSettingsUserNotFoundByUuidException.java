package hu.kalmancheysandor.applications.dominions.servers.admin.service.account.admin.settings.exception;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record.RecordNotFoundByUuidException;

public class AdminAccountSettingsUserNotFoundByUuidException extends RecordNotFoundByUuidException {
    public AdminAccountSettingsUserNotFoundByUuidException(String uuid) {
        super(uuid);
    }
}
