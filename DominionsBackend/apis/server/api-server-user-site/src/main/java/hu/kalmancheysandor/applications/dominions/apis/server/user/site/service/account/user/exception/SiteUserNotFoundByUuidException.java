package hu.kalmancheysandor.applications.dominions.apis.server.user.site.service.account.user.exception;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record.RecordNotFoundByUuidException;

public class SiteUserNotFoundByUuidException extends RecordNotFoundByUuidException {
    public SiteUserNotFoundByUuidException(String uuid) {
        super(uuid);
    }
}
