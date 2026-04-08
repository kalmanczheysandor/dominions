package hu.kalmancheysandor.applications.dominions.apis.server.user.admin.service.account.user.exception;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record.RecordNotFoundByUuidException;

public class AdminUserNotFoundByUuidException extends RecordNotFoundByUuidException {
    public AdminUserNotFoundByUuidException(String uuid) {
        super(uuid);
    }
}
