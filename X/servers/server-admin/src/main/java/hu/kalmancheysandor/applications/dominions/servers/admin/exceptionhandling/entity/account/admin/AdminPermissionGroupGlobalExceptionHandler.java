package hu.kalmancheysandor.applications.dominions.servers.admin.exceptionhandling.entity.account.admin;


import hu.kalmancheysandor.applications.dominions.apis.server.common.dto.error.GeneralErrorResponse;
import hu.kalmancheysandor.applications.dominions.apis.server.common.dto.failure.GeneralFailureResponse;
import hu.kalmancheysandor.applications.dominions.apis.server.user.admin.service.account.permission.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Order(1)
@Slf4j
@RestControllerAdvice
public class AdminPermissionGroupGlobalExceptionHandler {
    
    @ExceptionHandler(AdminPermissionGroupNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public GeneralFailureResponse handle(AdminPermissionGroupNotFoundException exception) {
        // Logging
        log.error("Permission-group not found: {}", exception.getMessage());

        // Generate response
        GeneralFailureResponse response = new GeneralFailureResponse(
            exception.getClass().getSimpleName(),
            exception.getMessage()
        );
        response.addParameter("id",exception.getId());

        return response;
    }

    @ExceptionHandler(AdminPermissionGroupNotFoundByUuidException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public GeneralFailureResponse handle(AdminPermissionGroupNotFoundByUuidException exception) {
        // Logging
        log.error("Permission-group not found by UUID: {}", exception.getUuid(), exception);

        // Generate response
        GeneralFailureResponse response = new GeneralFailureResponse(
            exception.getClass().getSimpleName(),
            exception.getMessage()
        );
        response.addParameter("uuid", exception.getUuid());

        return response;
    }

    @ExceptionHandler(AdminPermissionGroupNameIsReservedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GeneralFailureResponse handle(AdminPermissionGroupNameIsReservedException exception) {
        // Logging
        log.warn("Permission-group name is reserved: {}", exception.getName());

        // Generate response
        GeneralFailureResponse response = new GeneralFailureResponse(
            exception.getClass().getSimpleName(),
            exception.getMessage()
        );
        response.addParameter("name", exception.getName());

        return response;
    }

    @ExceptionHandler(AdminPermissionGroupForeignKeyViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)  // If Integrity conflict
    public GeneralErrorResponse handle(AdminPermissionGroupForeignKeyViolationException exception) {
        // Logging
        log.error("Foreign key constraint violation for PermissionGroup with ID: {}", exception.getId());

        // Generate response
        GeneralErrorResponse response = new GeneralErrorResponse(
            exception.getClass().getSimpleName(),
            "Foreign key constraint violation occurred for the Permission Group."
        );
        response.addParameter("id", exception.getId());

        return response;
    }

    @ExceptionHandler(AdminPermissionGroupAssociationRestrictedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GeneralFailureResponse handle(AdminPermissionGroupAssociationRestrictedException exception) {
        // Logging
        log.warn("PermissionGroup association is restricted. PermissionGroup id: {}.PermissionGroup name: {}",exception.getId(), exception.getName());

        // Generate response
        GeneralFailureResponse response = new GeneralFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());
        response.addParameter("name", exception.getName());

        return response;
    }






    @ExceptionHandler(AdminPermissionGroupReferencedElsewhereException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GeneralFailureResponse handle(AdminPermissionGroupReferencedElsewhereException exception) {
        // Logging
        log.warn("PermissionGroup with '{}' id is still referenced.PermissionGroup name: {}.", exception.getId(), exception.getName());

        // Generate response
        GeneralFailureResponse response = new GeneralFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());
        response.addParameter("name", exception.getName());

        return response;
    }

}
