package hu.kalmancheysandor.applications.dominions.service.account.user.exception;

import hu.kalmancheysandor.applications.dominions.service.exception.record.RecordNotFoundByUuidException;
import hu.kalmancheysandor.applications.dominions.service.exception.record.RecordNotFoundByUuidException;
import hu.kalmancheysandor.applications.dominions.service.exception.record.RecordNotFoundByUuidException;

public class UserNotFoundByUuidException extends RecordNotFoundByUuidException {
    public UserNotFoundByUuidException(String uuid) {
        super(uuid);
    }
}
