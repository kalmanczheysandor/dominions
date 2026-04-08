package hu.kalmancheysandor.applications.dominions.servers.site.service.account.settings.exception;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.CustomException;

public class SiteAccountSettingsCredentialConfirmPasswordMismatchException extends CustomException {
    public SiteAccountSettingsCredentialConfirmPasswordMismatchException() {
        super("UserConfirmPasswordMismatch");
    }
}
