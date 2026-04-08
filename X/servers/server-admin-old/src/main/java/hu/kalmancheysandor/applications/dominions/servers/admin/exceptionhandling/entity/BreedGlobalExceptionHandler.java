package hu.kalmancheysandor.applications.dominions.servers.admin.exceptionhandling.entity;

import hu.kalmancheysandor.applications.dominions.servers.admin.dto.failure.GeneralFailureResponse;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import lombok.extern.slf4j.Slf4j;

@Order(1)
@Slf4j
@RestControllerAdvice
public class BreedGlobalExceptionHandler {

    @ExceptionHandler(BreedNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public GeneralFailureResponse handle(BreedNotFoundException exception) {
        // Logging
        log.error("Breed not found: {}", exception.getMessage());

        // Generate response
        GeneralFailureResponse response = new GeneralFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());

        return response;
    }

    @ExceptionHandler(BreedNotFoundByUuidException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public GeneralFailureResponse handle(BreedNotFoundByUuidException exception) {
        // Logging
        log.error("Breed not found by UUID: {}", exception.getUuid(), exception);

        // Generate response
        GeneralFailureResponse response = new GeneralFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());
        response.addParameter("uuid", exception.getUuid());

        return response;
    }

    @ExceptionHandler(BreedNameIsReservedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GeneralFailureResponse handle(BreedNameIsReservedException exception) {
        // Logging
        log.warn("Breed name is reserved: {}", exception.getName());

        // Generate response
        GeneralFailureResponse response = new GeneralFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());
        response.addParameter("name", exception.getName());

        return response;
    }

    @ExceptionHandler(BreedAssociationRestrictedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GeneralFailureResponse handle(BreedAssociationRestrictedException exception) {
        // Logging
        log.warn("Breed association is restricted. Breed id: {}.Breed name: {}",exception.getId(), exception.getName());

        // Generate response
        GeneralFailureResponse response = new GeneralFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());
        response.addParameter("name", exception.getName());

        return response;
    }


    @ExceptionHandler(BreedReferencedElsewhereException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GeneralFailureResponse handle(BreedReferencedElsewhereException exception) {
        // Logging
        log.warn("Breed with '{}' id is still referenced.Breed name: {}.", exception.getId(), exception.getName());

        // Generate response
        GeneralFailureResponse response = new GeneralFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());
        response.addParameter("name", exception.getName());

        return response;
    }


}
