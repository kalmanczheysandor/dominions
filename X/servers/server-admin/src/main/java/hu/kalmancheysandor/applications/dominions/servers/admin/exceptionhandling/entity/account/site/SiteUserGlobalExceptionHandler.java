package hu.kalmancheysandor.applications.dominions.servers.admin.exceptionhandling.entity.account.site;


import hu.kalmancheysandor.applications.dominions.apis.server.common.dto.failure.GeneralFailureResponse;
import hu.kalmancheysandor.applications.dominions.apis.server.user.admin.service.account.user.exception.AdminUserConfirmPasswordMismatchException;
import hu.kalmancheysandor.applications.dominions.apis.server.user.site.service.account.user.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Order(1)
@Slf4j
@RestControllerAdvice
public class SiteUserGlobalExceptionHandler {

    @ExceptionHandler(SiteUserConfirmPasswordMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GeneralFailureResponse handle(SiteUserConfirmPasswordMismatchException exception) {
        // Logging
        log.error("User password mismatch: {}", exception.getMessage());

        // Generate response
        GeneralFailureResponse response = new GeneralFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());

        return response;
    }

    @ExceptionHandler(SiteUserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public GeneralFailureResponse handle(SiteUserNotFoundException exception) {
        // Logging
        log.error("User not found: {}", exception.getMessage());

        // Generate response
        GeneralFailureResponse response = new GeneralFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());
        response.addParameter("id",exception.getId());

        return response;
    }

    @ExceptionHandler(SiteUserNotFoundByUuidException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public GeneralFailureResponse handle(SiteUserNotFoundByUuidException exception) {
        // Logging
        log.error("User not found by UUID: {}", exception.getUuid(), exception);

        // Generate response
        GeneralFailureResponse response = new GeneralFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());
        response.addParameter("uuid", exception.getUuid());

        return response;
    }



    @ExceptionHandler(SiteUserIdentifierIsReservedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GeneralFailureResponse handle(SiteUserIdentifierIsReservedException exception) {
        // Logging
        log.warn("User identifier is reserved: {}", exception.getIdentifier());

        // Generate response
        GeneralFailureResponse response = new GeneralFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());
        response.addParameter("identifier", exception.getIdentifier());

        return response;
    }

    @ExceptionHandler(SiteUserAssociationRestrictedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GeneralFailureResponse handle(SiteUserAssociationRestrictedException exception) {
        // Logging
        log.warn("User association is restricted. User id: {}.User identifier: {}",exception.getId(), exception.getIdentifier());

        // Generate response
        GeneralFailureResponse response = new GeneralFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());
        response.addParameter("identifier", exception.getIdentifier());

        return response;
    }

    @ExceptionHandler(SiteUserReferencedElsewhereException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GeneralFailureResponse handle(SiteUserReferencedElsewhereException exception) {
        // Logging
        log.warn("User with '{}' id is still referenced.User identifier: {}.", exception.getId(), exception.getIdentifier());

        // Generate response
        GeneralFailureResponse response = new GeneralFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());
        response.addParameter("identifier", exception.getIdentifier());

        return response;
    }

    @ExceptionHandler(SiteUserSelfDeleteException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GeneralFailureResponse handle(SiteUserSelfDeleteException exception) {
        // Logging
        log.error("User attempted to delete themselves: {}", exception.getMessage());

        // Generate response
        GeneralFailureResponse response = new GeneralFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());
        return response;
    }




    @ExceptionHandler(SiteUserEnabledButNotFinalisedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GeneralFailureResponse handle(SiteUserEnabledButNotFinalisedException exception) {
        // Logging
        log.error("User enabled bot not finalised: {}", exception.getMessage());

        // Generate response
        GeneralFailureResponse response = new GeneralFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());

        return response;
    }
}
