package hu.kalmancheysandor.applications.dominions.servers.site.exceptionhandling.account;


import hu.kalmancheysandor.applications.dominions.apis.server.common.dto.failure.GeneralFailureResponse;
import hu.kalmancheysandor.applications.dominions.servers.site.service.account.exception.recovery.*;
import hu.kalmancheysandor.applications.dominions.servers.site.service.account.exception.signup.SiteAccountSignUpReservedUserIdentifierException;
import hu.kalmancheysandor.applications.dominions.servers.site.service.account.exception.signup.SiteAccountSignUpUserDetailsNotFoundException;
import hu.kalmancheysandor.applications.dominions.servers.site.service.account.exception.signup.SiteAccountSignUpUserUuidDuplicationException;
import hu.kalmancheysandor.applications.dominions.servers.site.service.account.exception.signup.SiteAccountSignUpVerificationTokenNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Order(1)
@Slf4j
@RestControllerAdvice
public class SiteAccountGlobalExceptionHandler {
    /// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /// //////// SIGNUP ///////////////////////////////////////////////////////////////////////////////////////////////
    /// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ExceptionHandler(SiteAccountSignUpVerificationTokenNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public GeneralFailureResponse handle(SiteAccountSignUpVerificationTokenNotFoundException exception) {
        log.error("Sign up verification token is not found: {}", exception.getMessage());
        GeneralFailureResponse response = new GeneralFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());
        response.addParameter("verificationToken", exception.getVerificationToken());
        return response;
    }

    @ExceptionHandler(SiteAccountSignUpUserDetailsNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public GeneralFailureResponse handle(SiteAccountSignUpUserDetailsNotFoundException exception) {
        log.error("Sign up user details is not found: {}", exception.getMessage());
        return new GeneralFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());
    }

    @ExceptionHandler(SiteAccountSignUpUserUuidDuplicationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public GeneralFailureResponse handle(SiteAccountSignUpUserUuidDuplicationException exception) {
        log.error("User UUID already exists: {}", exception.getMessage());
        GeneralFailureResponse response = new GeneralFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());
        response.addParameter("userUuid", exception.getUserUuid());
        return response;
    }


    @ExceptionHandler(SiteAccountSignUpReservedUserIdentifierException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public GeneralFailureResponse handle(SiteAccountSignUpReservedUserIdentifierException exception) {
        log.error("User identifier already exists: {}", exception.getMessage());
        GeneralFailureResponse response = new GeneralFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());
        response.addParameter("identifier", exception.getIdentifier());
        return response;
    }

    /// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /// //////// RECOVERY /////////////////////////////////////////////////////////////////////////////////////////////
    /// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ExceptionHandler(SiteAccountRecoveryVerificationTokenNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public GeneralFailureResponse handle(SiteAccountRecoveryVerificationTokenNotFoundException exception) {
        log.error("Recovery verification token is not found: {}", exception.getMessage());
        GeneralFailureResponse response = new GeneralFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());
        response.addParameter("verificationToken", exception.getVerificationToken());
        return response;
    }

    @ExceptionHandler(SiteAccountRecoveryUserDetailsNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public GeneralFailureResponse handle(SiteAccountRecoveryUserDetailsNotFoundException exception) {
        log.error("Recovery user details not found: {}", exception.getMessage());
        return new GeneralFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());
    }

    @ExceptionHandler(SiteAccountRecoveryUserUuidDuplicationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public GeneralFailureResponse handle(SiteAccountRecoveryUserUuidDuplicationException exception) {
        log.error("User UUID already exists: {}", exception.getMessage());
        GeneralFailureResponse response = new GeneralFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());
        response.addParameter("userUuid", exception.getUserUuid());
        return response;
    }

    @ExceptionHandler(SiteAccountRecoveryIdentifierNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public GeneralFailureResponse handle(SiteAccountRecoveryIdentifierNotFoundException exception) {
        log.error("User identifier not found: {}", exception.getMessage());
        GeneralFailureResponse response = new GeneralFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());
        response.addParameter("identifier", exception.getIdentifier());
        return response;
    }

    @ExceptionHandler(SiteAccountRecoveryUserNotFinalisedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public GeneralFailureResponse handle(SiteAccountRecoveryUserNotFinalisedException exception) {
        log.error("User is not finalised: {}", exception.getMessage());
        return new GeneralFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());
    }

    @ExceptionHandler(SiteAccountRecoveryUserNotEnabledException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public GeneralFailureResponse handle(SiteAccountRecoveryUserNotEnabledException exception) {
        log.error("User is not enabled: {}", exception.getMessage());
        return new GeneralFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());
    }

    @ExceptionHandler(SiteAccountRecoveryConfirmPasswordMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GeneralFailureResponse handle(SiteAccountRecoveryConfirmPasswordMismatchException exception) {
        log.error("Confirm password mismatch: {}", exception.getMessage());
        return new GeneralFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());
    }



}
