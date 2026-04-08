package hu.kalmancheysandor.applications.dominions.apis.server.user.admin.service.account.user.exception;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record.RecordNotFoundByIdException;

public class AdminUserNotFoundException extends RecordNotFoundByIdException {
    public AdminUserNotFoundException(int id) {
        super(id);
    }
}
