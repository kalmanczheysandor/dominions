package hu.kalmancheysandor.applications.dominions.service.account.profile.exception;

import hu.kalmancheysandor.applications.dominions.service.exception.CustomException;
import hu.kalmancheysandor.applications.dominions.service.exception.CustomException;
import hu.kalmancheysandor.applications.dominions.service.exception.CustomException;

public class ProfileConfirmPasswordMismatchException extends CustomException {
    public ProfileConfirmPasswordMismatchException() {
        super("UserConfirmPasswordMismatch");
    }
}
