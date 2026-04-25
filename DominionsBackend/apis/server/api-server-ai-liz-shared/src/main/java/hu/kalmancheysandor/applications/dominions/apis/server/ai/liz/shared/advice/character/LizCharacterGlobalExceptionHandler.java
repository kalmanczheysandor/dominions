package hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.advice.character;


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
    public GeneralFailureResponse handle(LizCharacterNotFoundException ex) {

        log.error("LizCharacter not found: {}", ex.getMessage(), ex);

        // Generate output
        GeneralFailureResponse response = new GeneralFailureResponse(
                ex.getClass().getSimpleName(),
                ex.getMessage()
        );

        return response;
    }

    @ExceptionHandler(LizCharacterNotFoundByUuidException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public GeneralFailureResponse handle(LizCharacterNotFoundByUuidException ex) {

        log.error("LizCharacter not found by UUID: {}", ex.getUuid(), ex);

        // Generate output
        GeneralFailureResponse response = new GeneralFailureResponse(
                ex.getClass().getSimpleName(),
                ex.getMessage()
        );
        response.addParameter("uuid", ex.getUuid());

        return response;
    }

    @ExceptionHandler(LizCharacterNotFoundByCodeException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public GeneralFailureResponse handle(LizCharacterNotFoundByCodeException ex) {

        log.error("LizCharacter not found by code: {}", ex.getCode(), ex);

        // Generate output
        GeneralFailureResponse response = new GeneralFailureResponse(
                ex.getClass().getSimpleName(),
                ex.getMessage()
        );
        response.addParameter("code", ex.getCode());

        return response;
    }

    @ExceptionHandler(LizCharacterAssociationRestrictedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GeneralFailureResponse handle(LizCharacterAssociationRestrictedException ex) {

        log.warn("LizCharacter association is restricted. Id: {}. Title: {}", ex.getId(), ex.getTitle(), ex);

        // Generate output
        GeneralFailureResponse response = new GeneralFailureResponse(
                ex.getClass().getSimpleName(),
                ex.getMessage()
        );
        response.addParameter("name", ex.getTitle());

        return response;
    }

    @ExceptionHandler(LizCharacterReferencedElsewhereException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GeneralFailureResponse handle(LizCharacterReferencedElsewhereException ex) {

        log.warn("LizCharacter with id '{}' is still referenced. Name: {}", ex.getId(), ex.getName(), ex);

        // Generate output
        GeneralFailureResponse response = new GeneralFailureResponse(
                ex.getClass().getSimpleName(),
                ex.getMessage()
        );
        response.addParameter("name", ex.getName());

        return response;
    }

    @ExceptionHandler(LizCharacterNameIsReservedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GeneralFailureResponse handle(LizCharacterNameIsReservedException ex) {

        log.warn("LizCharacter name is reserved: {}", ex.getName(), ex);

        // Generate output
        GeneralFailureResponse response = new GeneralFailureResponse(
                ex.getClass().getSimpleName(),
                ex.getMessage()
        );
        response.addParameter("name", ex.getName());

        return response;
    }

    @ExceptionHandler(LizCharacterCodeIsReservedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GeneralFailureResponse handle(LizCharacterCodeIsReservedException ex) {

        log.warn("LizCharacter code is reserved: {}", ex.getCode(), ex);

        // Generate output
        GeneralFailureResponse response = new GeneralFailureResponse(
                ex.getClass().getSimpleName(),
                ex.getMessage()
        );
        response.addParameter("code", ex.getCode());

        return response;
    }

}
