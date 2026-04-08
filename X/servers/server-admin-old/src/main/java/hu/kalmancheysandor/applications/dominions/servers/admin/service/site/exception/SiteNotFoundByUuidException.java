package hu.kalmancheysandor.applications.dominions.service.site.exception;

import hu.kalmancheysandor.applications.dominions.service.exception.record.RecordNotFoundByUuidException;
import hu.kalmancheysandor.applications.dominions.service.exception.record.RecordNotFoundByUuidException;
import hu.kalmancheysandor.applications.dominions.service.exception.record.RecordNotFoundByUuidException;

public class SiteNotFoundByUuidException extends RecordNotFoundByUuidException {
    public SiteNotFoundByUuidException(String uuid) {
        super(uuid);
    }
}
