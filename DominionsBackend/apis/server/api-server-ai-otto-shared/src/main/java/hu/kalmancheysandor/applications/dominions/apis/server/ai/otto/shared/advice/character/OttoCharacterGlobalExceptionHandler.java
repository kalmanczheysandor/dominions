package hu.kalmancheysandor.applications.dominions.apis.server.ai.otto.shared.advice.character;


import hu.kalmancheysandor.applications.dominions.apis.server.ai.otto.shared.exception.character.*;

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
public class OttoCharacterGlobalExceptionHandler {

    @ExceptionHandler(OttoCharacterNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public GeneralFailureResponse handle(OttoCharacterNotFoundException ex) {

        log.error("OttoCharacter not found: {}", ex.getMessage(), ex);

        // Generate output
        GeneralFailureResponse response = new GeneralFailureResponse(
                ex.getClass().getSimpleName(),
                ex.getMessage()
        );

        return response;
    }

    @ExceptionHandler(OttoCharacterNotFoundByUuidException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public GeneralFailureResponse handle(OttoCharacterNotFoundByUuidException ex) {

        log.error("OttoCharacter not found by UUID: {}", ex.getUuid(), ex);

        // Generate output
        GeneralFailureResponse response = new GeneralFailureResponse(
                ex.getClass().getSimpleName(),
                ex.getMessage()
        );
        response.addParameter("uuid", ex.getUuid());

        return response;
    }

    @ExceptionHandler(OttoCharacterNotFoundByCodeException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public GeneralFailureResponse handle(OttoCharacterNotFoundByCodeException ex) {

        log.error("OttoCharacter not found by code: {}", ex.getCode(), ex);

        // Generate output
        GeneralFailureResponse response = new GeneralFailureResponse(
                ex.getClass().getSimpleName(),
                ex.getMessage()
        );
        response.addParameter("code", ex.getCode());

        return response;
    }

    @ExceptionHandler(OttoCharacterAssociationRestrictedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GeneralFailureResponse handle(OttoCharacterAssociationRestrictedException ex) {

        log.warn("OttoCharacter association is restricted. Id: {}. Title: {}", ex.getId(), ex.getTitle(), ex);

        // Generate output
        GeneralFailureResponse response = new GeneralFailureResponse(
                ex.getClass().getSimpleName(),
                ex.getMessage()
        );
        response.addParameter("name", ex.getTitle());

        return response;
    }

    @ExceptionHandler(OttoCharacterReferencedElsewhereException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GeneralFailureResponse handle(OttoCharacterReferencedElsewhereException ex) {

        log.warn("OttoCharacter with id '{}' is still referenced. Name: {}", ex.getId(), ex.getName(), ex);

        // Generate output
        GeneralFailureResponse response = new GeneralFailureResponse(
                ex.getClass().getSimpleName(),
                ex.getMessage()
        );
        response.addParameter("name", ex.getName());

        return response;
    }

    @ExceptionHandler(OttoCharacterNameIsReservedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GeneralFailureResponse handle(OttoCharacterNameIsReservedException ex) {

        log.warn("OttoCharacter name is reserved: {}", ex.getName(), ex);

        // Generate output
        GeneralFailureResponse response = new GeneralFailureResponse(
                ex.getClass().getSimpleName(),
                ex.getMessage()
        );
        response.addParameter("name", ex.getName());

        return response;
    }

    @ExceptionHandler(OttoCharacterCodeIsReservedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GeneralFailureResponse handle(OttoCharacterCodeIsReservedException ex) {

        log.warn("OttoCharacter code is reserved: {}", ex.getCode(), ex);

        // Generate output
        GeneralFailureResponse response = new GeneralFailureResponse(
                ex.getClass().getSimpleName(),
                ex.getMessage()
        );
        response.addParameter("code", ex.getCode());

        return response;
    }

}
