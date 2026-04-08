package hu.kalmancheysandor.applications.dominions.servers.ai.liz.operation.exceptionhandling.entity.variant;


import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.exception.character.*;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.exception.variant.*;
import hu.kalmancheysandor.applications.dominions.apis.server.common.dto.failure.GeneralFailureResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Order(1)
@Slf4j
@RestControllerAdvice
public class LizVariantGlobalExceptionHandler {

    @ExceptionHandler(LizVariantNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public GeneralFailureResponse handle(LizVariantNotFoundException exception) {
        // Logging
        log.error("LizVariant not found: {}", exception.getMessage());

        // Generate response
        GeneralFailureResponse response = new GeneralFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());

        return response;
    }

    @ExceptionHandler(LizVariantNotFoundByUuidException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public GeneralFailureResponse handle(LizVariantNotFoundByUuidException exception) {
        // Logging
        log.error("LizVariant not found by UUID: {}", exception.getUuid(), exception);

        // Generate response
        GeneralFailureResponse response = new GeneralFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());
        response.addParameter("uuid", exception.getUuid());

        return response;
    }

    @ExceptionHandler(LizVariantAssociationRestrictedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GeneralFailureResponse handle(LizVariantAssociationRestrictedException exception) {
        // Logging
        log.warn("LizVariant association is restricted. LizVariant id: {}. LizVariant title: {}",exception.getId(), exception.getTitle());

        // Generate response
        GeneralFailureResponse response = new GeneralFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());
        response.addParameter("name", exception.getTitle());

        return response;
    }

    @ExceptionHandler(LizVariantReferencedElsewhereException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GeneralFailureResponse handle(LizVariantReferencedElsewhereException exception) {
        // Logging
        log.warn("LizVariant with '{}' id is still referenced with. LizVariant name: {}.", exception.getId(), exception.getName());

        // Generate response
        GeneralFailureResponse response = new GeneralFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());
        response.addParameter("name", exception.getName());

        return response;
    }

    @ExceptionHandler(LizVariantNameIsReservedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GeneralFailureResponse handle(LizVariantNameIsReservedException exception) {
        // Logging
        log.warn("LizVariant name is reserved: {}", exception.getName());

        // Generate response
        GeneralFailureResponse response = new GeneralFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());
        response.addParameter("name", exception.getName());

        return response;
    }






}
