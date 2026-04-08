package hu.kalmancheysandor.applications.dominions.servers.admin.service.account.admin.settings.exception;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.CustomException;

public class AdminAccountSettingsCredentialConfirmPasswordMismatchException extends CustomException {
    public AdminAccountSettingsCredentialConfirmPasswordMismatchException() {
        super("UserConfirmPasswordMismatch");
    }
}
