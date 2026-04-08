package hu.kalmancheysandor.applications.dominions.servers.site.service.account.settings.exception;

import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.CustomException;

public class SiteAccountSettingsCredentialCurrentPasswordMismatchException extends CustomException {
    public SiteAccountSettingsCredentialCurrentPasswordMismatchException() {
        super("ProfileCurrentPasswordMismatch");
    }
}
