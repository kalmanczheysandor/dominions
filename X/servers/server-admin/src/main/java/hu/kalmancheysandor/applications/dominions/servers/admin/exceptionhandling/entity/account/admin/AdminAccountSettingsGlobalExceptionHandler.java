package hu.kalmancheysandor.applications.dominions.servers.admin.exceptionhandling.entity.account.admin;


import hu.kalmancheysandor.applications.dominions.apis.server.common.dto.failure.GeneralFailureResponse;
import hu.kalmancheysandor.applications.dominions.servers.admin.service.account.admin.settings.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Order(1)
@Slf4j
@RestControllerAdvice
public class AdminAccountSettingsGlobalExceptionHandler {

    @ExceptionHandler(AdminAccountSettingsCredentialCurrentPasswordMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GeneralFailureResponse handle(AdminAccountSettingsCredentialCurrentPasswordMismatchException exception) {
        // Logging
        log.error("Profile password mismatch with current one: {}", exception.getMessage());

        // Generate response
        GeneralFailureResponse response = new GeneralFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());

        return response;
    }

    @ExceptionHandler(AdminAccountSettingsCredentialConfirmPasswordMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GeneralFailureResponse handle(AdminAccountSettingsCredentialConfirmPasswordMismatchException exception) {
        // Logging
        log.error("Profile password mismatch: {}", exception.getMessage());

        // Generate response
        GeneralFailureResponse response = new GeneralFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());

        return response;
    }

    @ExceptionHandler(AdminAccountSettingsUserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public GeneralFailureResponse handle(AdminAccountSettingsUserNotFoundException exception) {
        // Logging
        log.error("Profile not found: {}", exception.getMessage());

        // Generate response
        GeneralFailureResponse response = new GeneralFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());
        response.addParameter("id",exception.getId());

        return response;
    }

    @ExceptionHandler(AdminAccountSettingsUserNotFoundByUuidException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public GeneralFailureResponse handle(AdminAccountSettingsUserNotFoundByUuidException exception) {
        // Logging
        log.error("Profile not found by UUID: {}", exception.getUuid(), exception);

        // Generate response
        GeneralFailureResponse response = new GeneralFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());
        response.addParameter("uuid", exception.getUuid());

        return response;
    }

    @ExceptionHandler(AdminAccountSettingsIdentifierIsReservedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GeneralFailureResponse handle(AdminAccountSettingsIdentifierIsReservedException exception) {
        // Logging
        log.warn("Profile identifier is reserved: {}", exception.getIdentifier());

        // Generate response
        GeneralFailureResponse response = new GeneralFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());
        response.addParameter("identifier", exception.getIdentifier());

        return response;
    }
}
