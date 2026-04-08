package hu.kalmancheysandor.applications.dominions.service.account.profile.exception;

import hu.kalmancheysandor.applications.dominions.service.exception.record.RecordNotFoundByUuidException;
import hu.kalmancheysandor.applications.dominions.service.exception.record.RecordNotFoundByUuidException;
import hu.kalmancheysandor.applications.dominions.service.exception.record.RecordNotFoundByUuidException;

public class ProfileNotFoundByUuidException extends RecordNotFoundByUuidException {
    public ProfileNotFoundByUuidException(String uuid) {
        super(uuid);
    }
}
