package hu.kalmancheysandor.applications.dominions.servers.admin.exceptionhandling.entity;

import hu.kalmancheysandor.applications.dominions.servers.admin.dto.failure.GeneralFailureResponse;
import hu.kalmancheysandor.applications.dominions.servers.admin.service.account.user.exception.UserSelfDeleteException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Order(1)
@Slf4j
@RestControllerAdvice
public class UserGlobalExceptionHandler {

    @ExceptionHandler(UserConfirmPasswordMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GeneralFailureResponse handle(UserConfirmPasswordMismatchException exception) {
        // Logging
        log.error("User password mismatch: {}", exception.getMessage());

        // Generate response
        GeneralFailureResponse response = new GeneralFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());

        return response;
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public GeneralFailureResponse handle(UserNotFoundException exception) {
        // Logging
        log.error("User not found: {}", exception.getMessage());

        // Generate response
        GeneralFailureResponse response = new GeneralFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());
        response.addParameter("id",exception.getId());

        return response;
    }

    @ExceptionHandler(UserNotFoundByUuidException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public GeneralFailureResponse handle(UserNotFoundByUuidException exception) {
        // Logging
        log.error("User not found by UUID: {}", exception.getUuid(), exception);

        // Generate response
        GeneralFailureResponse response = new GeneralFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());
        response.addParameter("uuid", exception.getUuid());

        return response;
    }



    @ExceptionHandler(UserIdentifierIsReservedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GeneralFailureResponse handle(UserIdentifierIsReservedException exception) {
        // Logging
        log.warn("User identifier is reserved: {}", exception.getIdentifier());

        // Generate response
        GeneralFailureResponse response = new GeneralFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());
        response.addParameter("identifier", exception.getIdentifier());

        return response;
    }

    @ExceptionHandler(UserAssociationRestrictedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GeneralFailureResponse handle(UserAssociationRestrictedException exception) {
        // Logging
        log.warn("User association is restricted. User id: {}.User identifier: {}",exception.getId(), exception.getIdentifier());

        // Generate response
        GeneralFailureResponse response = new GeneralFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());
        response.addParameter("identifier", exception.getIdentifier());

        return response;
    }

    @ExceptionHandler(UserReferencedElsewhereException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GeneralFailureResponse handle(UserReferencedElsewhereException exception) {
        // Logging
        log.warn("User with '{}' id is still referenced.User identifier: {}.", exception.getId(), exception.getIdentifier());

        // Generate response
        GeneralFailureResponse response = new GeneralFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());
        response.addParameter("identifier", exception.getIdentifier());

        return response;
    }

    @ExceptionHandler(UserSelfDeleteException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GeneralFailureResponse handle(UserSelfDeleteException exception) {
        // Logging
        log.error("User attempted to delete themselves: {}", exception.getMessage());

        // Generate response
        GeneralFailureResponse response = new GeneralFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());
        return response;
    }


}
