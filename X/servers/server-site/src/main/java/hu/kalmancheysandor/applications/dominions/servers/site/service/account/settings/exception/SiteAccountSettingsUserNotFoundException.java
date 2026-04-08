package hu.kalmancheysandor.applications.dominions.servers.site.service.account.settings.exception;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record.RecordNotFoundByIdException;

public class SiteAccountSettingsUserNotFoundException extends RecordNotFoundByIdException {
    public SiteAccountSettingsUserNotFoundException(int id) {
        super(id);
    }
}
