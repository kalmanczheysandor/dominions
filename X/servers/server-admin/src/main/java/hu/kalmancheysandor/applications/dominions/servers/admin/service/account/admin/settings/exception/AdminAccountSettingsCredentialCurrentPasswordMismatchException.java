package hu.kalmancheysandor.applications.dominions.servers.admin.service.account.admin.settings.exception;

import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.CustomException;

public class AdminAccountSettingsCredentialCurrentPasswordMismatchException extends CustomException {
    public AdminAccountSettingsCredentialCurrentPasswordMismatchException() {
        super("ProfileCurrentPasswordMismatch");
    }
}
