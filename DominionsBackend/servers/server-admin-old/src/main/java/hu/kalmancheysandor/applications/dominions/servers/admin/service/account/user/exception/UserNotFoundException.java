package hu.kalmancheysandor.applications.dominions.service.account.user.exception;

import hu.kalmancheysandor.applications.dominions.service.exception.record.RecordNotFoundByIdException;
import hu.kalmancheysandor.applications.dominions.service.exception.record.RecordNotFoundByIdException;
import hu.kalmancheysandor.applications.dominions.service.exception.record.RecordNotFoundByIdException;

public class UserNotFoundException extends RecordNotFoundByIdException {
    public UserNotFoundException(int id) {
        super(id);
    }
}
