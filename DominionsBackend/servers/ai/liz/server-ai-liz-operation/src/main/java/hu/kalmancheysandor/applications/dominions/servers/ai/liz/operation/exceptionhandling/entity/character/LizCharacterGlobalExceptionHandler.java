package hu.kalmancheysandor.applications.dominions.servers.ai.liz.operation.exceptionhandling.entity.character;


import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.exception.character.*;
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
public class LizCharacterGlobalExceptionHandler {

    @ExceptionHandler(LizCharacterNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public GeneralFailureResponse handle(LizCharacterNotFoundException exception) {
        // Logging
        log.error("LizCharacter not found: {}", exception.getMessage());

        // Generate response
        GeneralFailureResponse response = new GeneralFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());

        return response;
    }

    @ExceptionHandler(LizCharacterNotFoundByUuidException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public GeneralFailureResponse handle(LizCharacterNotFoundByUuidException exception) {
        // Logging
        log.error("LizCharacter not found by UUID: {}", exception.getUuid(), exception);

        // Generate response
        GeneralFailureResponse response = new GeneralFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());
        response.addParameter("uuid", exception.getUuid());

        return response;
    }

    @ExceptionHandler(LizCharacterNotFoundByCodeException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public GeneralFailureResponse handle(LizCharacterNotFoundByCodeException exception) {
        // Logging
        log.error("LizCharacter not found by code: {}", exception.getCode(), exception);

        // Generate response
        GeneralFailureResponse response = new GeneralFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());
        response.addParameter("code", exception.getCode());

        return response;
    }
    @ExceptionHandler(LizCharacterAssociationRestrictedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GeneralFailureResponse handle(LizCharacterAssociationRestrictedException exception) {
        // Logging
        log.warn("LizCharacter association is restricted. LizCharacter id: {}. LizCharacter title: {}",exception.getId(), exception.getTitle());

        // Generate response
        GeneralFailureResponse response = new GeneralFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());
        response.addParameter("name", exception.getTitle());

        return response;
    }

    @ExceptionHandler(LizCharacterReferencedElsewhereException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GeneralFailureResponse handle(LizCharacterReferencedElsewhereException exception) {
        // Logging
        log.warn("LizCharacter with '{}' id is still referenced with. LizCharacter name: {}.", exception.getId(), exception.getName());

        // Generate response
        GeneralFailureResponse response = new GeneralFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());
        response.addParameter("name", exception.getName());

        return response;
    }

    @ExceptionHandler(LizCharacterNameIsReservedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GeneralFailureResponse handle(LizCharacterNameIsReservedException exception) {
        // Logging
        log.warn("LizCharacter name is reserved: {}", exception.getName());

        // Generate response
        GeneralFailureResponse response = new GeneralFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());
        response.addParameter("name", exception.getName());

        return response;
    }

    @ExceptionHandler(LizCharacterCodeIsReservedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GeneralFailureResponse handle(LizCharacterCodeIsReservedException exception) {
        // Logging
        log.warn("LizCharacter code is reserved: {}", exception.getCode());

        // Generate response
        GeneralFailureResponse response = new GeneralFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());
        response.addParameter("code", exception.getCode());

        return response;
    }






}
