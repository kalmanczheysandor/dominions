package hu.kalmancheysandor.applications.dominions.apis.server.ai.otto.shared.advice.variant;


import hu.kalmancheysandor.applications.dominions.apis.server.ai.otto.shared.exception.variant.*;
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
public class OttoVariantGlobalExceptionHandler {

    @ExceptionHandler(OttoVariantNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public GeneralFailureResponse handle(OttoVariantNotFoundException ex) {

        log.error("OttoVariant not found: {}", ex.getMessage(), ex);

        // Generate output
        GeneralFailureResponse response = new GeneralFailureResponse(
                ex.getClass().getSimpleName(),
                ex.getMessage()
        );

        return response;
    }

    @ExceptionHandler(OttoVariantNotFoundByUuidException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public GeneralFailureResponse handle(OttoVariantNotFoundByUuidException ex) {

        log.error("OttoVariant not found by UUID: {}", ex.getUuid(), ex);

        // Generate output
        GeneralFailureResponse response = new GeneralFailureResponse(
                ex.getClass().getSimpleName(),
                ex.getMessage()
        );
        response.addParameter("uuid", ex.getUuid());

        return response;
    }

    @ExceptionHandler(OttoVariantAssociationRestrictedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GeneralFailureResponse handle(OttoVariantAssociationRestrictedException ex) {

        log.warn("OttoVariant association is restricted. Id: {}. Title: {}", ex.getId(), ex.getTitle(), ex);

        // Generate output
        GeneralFailureResponse response = new GeneralFailureResponse(
                ex.getClass().getSimpleName(),
                ex.getMessage()
        );
        response.addParameter("name", ex.getTitle());

        return response;
    }

    @ExceptionHandler(OttoVariantReferencedElsewhereException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GeneralFailureResponse handle(OttoVariantReferencedElsewhereException ex) {

        log.warn("OttoVariant with id '{}' is still referenced. Name: {}", ex.getId(), ex.getName(), ex);

        // Generate output
        GeneralFailureResponse response = new GeneralFailureResponse(
                ex.getClass().getSimpleName(),
                ex.getMessage()
        );
        response.addParameter("name", ex.getName());

        return response;
    }

    @ExceptionHandler(OttoVariantNameIsReservedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GeneralFailureResponse handle(OttoVariantNameIsReservedException ex) {

        log.warn("OttoVariant name is reserved: {}", ex.getName(), ex);

        // Generate output
        GeneralFailureResponse response = new GeneralFailureResponse(
                ex.getClass().getSimpleName(),
                ex.getMessage()
        );
        response.addParameter("name", ex.getName());

        return response;
    }

}
