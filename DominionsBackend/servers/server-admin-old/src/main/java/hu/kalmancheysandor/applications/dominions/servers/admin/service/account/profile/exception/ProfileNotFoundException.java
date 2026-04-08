package hu.kalmancheysandor.applications.dominions.service.account.profile.exception;

import hu.kalmancheysandor.applications.dominions.service.exception.record.RecordNotFoundByIdException;
import hu.kalmancheysandor.applications.dominions.service.exception.record.RecordNotFoundByIdException;
import hu.kalmancheysandor.applications.dominions.service.exception.record.RecordNotFoundByIdException;

public class ProfileNotFoundException extends RecordNotFoundByIdException {
    public ProfileNotFoundException(int id) {
        super(id);
    }
}
