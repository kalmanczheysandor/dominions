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
public class DogGlobalExceptionHandler {

    @ExceptionHandler(DogNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public GeneralFailureResponse handle(DogNotFoundException exception) {
        // Logging
        log.error("Dog not found: {}", exception.getMessage());

        // Generate response
        GeneralFailureResponse response = new GeneralFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());

        return response;
    }

    @ExceptionHandler(DogNotFoundByUuidException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public GeneralFailureResponse handle(DogNotFoundByUuidException exception) {
        // Logging
        log.error("Dog not found by UUID: {}", exception.getUuid(), exception);
        // Generate response
        GeneralFailureResponse response = new GeneralFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());
        response.addParameter("uuid", exception.getUuid());
        return response;
    }

    @ExceptionHandler(DogPrnIsReservedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GeneralFailureResponse handle(DogPrnIsReservedException exception) {
        // Logging
        log.warn("Dog prn is reserved: {}", exception.getPrn());

        // Generate response
        GeneralFailureResponse response = new GeneralFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());
        response.addParameter("prn", exception.getPrn());

        return response;
    }



    @ExceptionHandler(DogAssociationRestrictedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GeneralFailureResponse handle(DogAssociationRestrictedException exception) {
        // Logging
        log.warn("Dog association is restricted. Dog id: {}.Dog name: {}",exception.getId(), exception.getName());

        // Generate response
        GeneralFailureResponse response = new GeneralFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());
        response.addParameter("name", exception.getName());

        return response;
    }


    @ExceptionHandler(DogReferencedElsewhereException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GeneralFailureResponse handle(DogReferencedElsewhereException exception) {
        // Logging
        log.warn("Dog with '{}' id is still referenced. Dog name: {}.", exception.getId(), exception.getName());

        // Generate response
        GeneralFailureResponse response = new GeneralFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());
        response.addParameter("name", exception.getName());

        return response;
    }
}
