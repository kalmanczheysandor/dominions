package hu.kalmancheysandor.applications.dominions.servers.site.exceptionhandling.account.settings;


import hu.kalmancheysandor.applications.dominions.apis.server.common.dto.failure.GeneralFailureResponse;
import hu.kalmancheysandor.applications.dominions.servers.site.service.account.settings.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Order(1)
@Slf4j
@RestControllerAdvice
public class SiteAccountSettingsGlobalExceptionHandler {


    @ExceptionHandler(SiteAccountSettingsCredentialCurrentPasswordMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GeneralFailureResponse handle(SiteAccountSettingsCredentialCurrentPasswordMismatchException exception) {
        // Logging
        log.error("Profile password mismatch with current one: {}", exception.getMessage());

        // Generate response
        GeneralFailureResponse response = new GeneralFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());

        return response;
    }

    @ExceptionHandler(SiteAccountSettingsCredentialConfirmPasswordMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GeneralFailureResponse handle(SiteAccountSettingsCredentialConfirmPasswordMismatchException exception) {
        // Logging
        log.error("Profile password mismatch: {}", exception.getMessage());

        // Generate response
        GeneralFailureResponse response = new GeneralFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());

        return response;
    }

    @ExceptionHandler(SiteAccountSettingsUserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public GeneralFailureResponse handle(SiteAccountSettingsUserNotFoundException exception) {
        // Logging
        log.error("Profile not found: {}", exception.getMessage());

        // Generate response
        GeneralFailureResponse response = new GeneralFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());
        response.addParameter("id",exception.getId());

        return response;
    }

    @ExceptionHandler(SiteAccountSettingsUserNotFoundByUuidException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public GeneralFailureResponse handle(SiteAccountSettingsUserNotFoundByUuidException exception) {
        // Logging
        log.error("Profile not found by UUID: {}", exception.getUuid(), exception);

        // Generate response
        GeneralFailureResponse response = new GeneralFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());
        response.addParameter("uuid", exception.getUuid());

        return response;
    }



    @ExceptionHandler(SiteAccountSettingsIdentifierIsReservedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GeneralFailureResponse handle(SiteAccountSettingsIdentifierIsReservedException exception) {
        // Logging
        log.warn("Profile identifier is reserved: {}", exception.getIdentifier());

        // Generate response
        GeneralFailureResponse response = new GeneralFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());
        response.addParameter("identifier", exception.getIdentifier());

        return response;
    }
//
//    @ExceptionHandler(SiteAccountSettingsAssociationRestrictedException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public GeneralFailureResponse handle(SiteAccountSettingsAssociationRestrictedException exception) {
//        // Logging
//        log.warn("Profile association is restricted. Profile id: {}.Profile identifier: {}",exception.getId(), exception.getIdentifier());
//
//        // Generate response
//        GeneralFailureResponse response = new GeneralFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());
//        response.addParameter("identifier", exception.getIdentifier());
//
//        return response;
//    }
//
//    @ExceptionHandler(SiteAccountSettingsReferencedElsewhereException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public GeneralFailureResponse handle(SiteAccountSettingsReferencedElsewhereException exception) {
//        // Logging
//        log.warn("Profile with '{}' id is still referenced.Profile identifier: {}.", exception.getId(), exception.getIdentifier());
//
//        // Generate response
//        GeneralFailureResponse response = new GeneralFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());
//        response.addParameter("identifier", exception.getIdentifier());
//
//        return response;
//    }


}
