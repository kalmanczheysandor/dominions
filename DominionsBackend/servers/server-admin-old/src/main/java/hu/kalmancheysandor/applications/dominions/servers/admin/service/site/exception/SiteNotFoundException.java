package hu.kalmancheysandor.applications.dominions.service.site.exception;

import hu.kalmancheysandor.applications.dominions.service.exception.record.RecordNotFoundByIdException;
import hu.kalmancheysandor.applications.dominions.service.exception.record.RecordNotFoundByIdException;
import hu.kalmancheysandor.applications.dominions.service.exception.record.RecordNotFoundByIdException;

public class SiteNotFoundException extends RecordNotFoundByIdException {
    public SiteNotFoundException(int id) {
        super(id);
    }
}
