package hu.kalmancheysandor.applications.dominions.servers.ai.hugo.operation.exceptionhandling.entity.character;


import hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.exception.character.*;
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
public class HugoCharacterGlobalExceptionHandler {

    @ExceptionHandler(HugoCharacterNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public GeneralFailureResponse handle(HugoCharacterNotFoundException exception) {
        // Logging
        log.error("HugoCharacter not found: {}", exception.getMessage());

        // Generate response
        GeneralFailureResponse response = new GeneralFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());

        return response;
    }

    @ExceptionHandler(HugoCharacterNotFoundByUuidException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public GeneralFailureResponse handle(HugoCharacterNotFoundByUuidException exception) {
        // Logging
        log.error("HugoCharacter not found by UUID: {}", exception.getUuid(), exception);

        // Generate response
        GeneralFailureResponse response = new GeneralFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());
        response.addParameter("uuid", exception.getUuid());

        return response;
    }

    @ExceptionHandler(HugoCharacterAssociationRestrictedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GeneralFailureResponse handle(HugoCharacterAssociationRestrictedException exception) {
        // Logging
        log.warn("HugoCharacter association is restricted. HugoCharacter id: {}. HugoCharacter title: {}",exception.getId(), exception.getTitle());

        // Generate response
        GeneralFailureResponse response = new GeneralFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());
        response.addParameter("name", exception.getTitle());

        return response;
    }

    @ExceptionHandler(HugoCharacterReferencedElsewhereException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GeneralFailureResponse handle(HugoCharacterReferencedElsewhereException exception) {
        // Logging
        log.warn("HugoCharacter with '{}' id is still referenced with. HugoCharacter name: {}.", exception.getId(), exception.getName());

        // Generate response
        GeneralFailureResponse response = new GeneralFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());
        response.addParameter("name", exception.getName());

        return response;
    }

    @ExceptionHandler(HugoCharacterNameIsReservedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GeneralFailureResponse handle(HugoCharacterNameIsReservedException exception) {
        // Logging
        log.warn("HugoCharacter name is reserved: {}", exception.getName());

        // Generate response
        GeneralFailureResponse response = new GeneralFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());
        response.addParameter("name", exception.getName());

        return response;
    }

    @ExceptionHandler(HugoCharacterCodeIsReservedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GeneralFailureResponse handle(HugoCharacterCodeIsReservedException exception) {
        // Logging
        log.warn("HugoCharacter code is reserved: {}", exception.getCode());

        // Generate response
        GeneralFailureResponse response = new GeneralFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());
        response.addParameter("code", exception.getCode());

        return response;
    }






}
