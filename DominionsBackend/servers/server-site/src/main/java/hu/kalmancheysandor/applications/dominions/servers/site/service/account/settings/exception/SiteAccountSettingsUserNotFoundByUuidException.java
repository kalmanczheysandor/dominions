package hu.kalmancheysandor.applications.dominions.servers.site.service.account.settings.exception;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record.RecordNotFoundByUuidException;

public class SiteAccountSettingsUserNotFoundByUuidException extends RecordNotFoundByUuidException {
    public SiteAccountSettingsUserNotFoundByUuidException(String uuid) {
        super(uuid);
    }
}
