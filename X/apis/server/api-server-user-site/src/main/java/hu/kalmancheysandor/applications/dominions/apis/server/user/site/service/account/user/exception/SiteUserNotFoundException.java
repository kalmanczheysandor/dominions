package hu.kalmancheysandor.applications.dominions.apis.server.user.site.service.account.user.exception;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record.RecordNotFoundByIdException;

public class SiteUserNotFoundException extends RecordNotFoundByIdException {
    public SiteUserNotFoundException(int id) {
        super(id);
    }
}
