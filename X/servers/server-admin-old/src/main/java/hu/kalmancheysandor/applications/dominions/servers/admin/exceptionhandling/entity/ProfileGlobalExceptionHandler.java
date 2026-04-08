package hu.kalmancheysandor.applications.dominions.servers.admin.exceptionhandling.entity;

import hu.kalmancheysandor.applications.dominions.servers.admin.dto.failure.GeneralFailureResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Order(1)
@Slf4j
@RestControllerAdvice
public class ProfileGlobalExceptionHandler {


    @ExceptionHandler(ProfileCurrentPasswordMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GeneralFailureResponse handle(ProfileCurrentPasswordMismatchException exception) {
        // Logging
        log.error("Profile password mismatch with current one: {}", exception.getMessage());

        // Generate response
        GeneralFailureResponse response = new GeneralFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());

        return response;
    }

    @ExceptionHandler(ProfileConfirmPasswordMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GeneralFailureResponse handle(ProfileConfirmPasswordMismatchException exception) {
        // Logging
        log.error("Profile password mismatch: {}", exception.getMessage());

        // Generate response
        GeneralFailureResponse response = new GeneralFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());

        return response;
    }

    @ExceptionHandler(ProfileNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public GeneralFailureResponse handle(ProfileNotFoundException exception) {
        // Logging
        log.error("Profile not found: {}", exception.getMessage());

        // Generate response
        GeneralFailureResponse response = new GeneralFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());
        response.addParameter("id",exception.getId());

        return response;
    }

    @ExceptionHandler(ProfileNotFoundByUuidException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public GeneralFailureResponse handle(ProfileNotFoundByUuidException exception) {
        // Logging
        log.error("Profile not found by UUID: {}", exception.getUuid(), exception);

        // Generate response
        GeneralFailureResponse response = new GeneralFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());
        response.addParameter("uuid", exception.getUuid());

        return response;
    }



    @ExceptionHandler(ProfileIdentifierIsReservedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GeneralFailureResponse handle(ProfileIdentifierIsReservedException exception) {
        // Logging
        log.warn("Profile identifier is reserved: {}", exception.getIdentifier());

        // Generate response
        GeneralFailureResponse response = new GeneralFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());
        response.addParameter("identifier", exception.getIdentifier());

        return response;
    }

    @ExceptionHandler(ProfileAssociationRestrictedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GeneralFailureResponse handle(ProfileAssociationRestrictedException exception) {
        // Logging
        log.warn("Profile association is restricted. Profile id: {}.Profile identifier: {}",exception.getId(), exception.getIdentifier());

        // Generate response
        GeneralFailureResponse response = new GeneralFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());
        response.addParameter("identifier", exception.getIdentifier());

        return response;
    }

    @ExceptionHandler(ProfileReferencedElsewhereException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GeneralFailureResponse handle(ProfileReferencedElsewhereException exception) {
        // Logging
        log.warn("Profile with '{}' id is still referenced.Profile identifier: {}.", exception.getId(), exception.getIdentifier());

        // Generate response
        GeneralFailureResponse response = new GeneralFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());
        response.addParameter("identifier", exception.getIdentifier());

        return response;
    }


}
