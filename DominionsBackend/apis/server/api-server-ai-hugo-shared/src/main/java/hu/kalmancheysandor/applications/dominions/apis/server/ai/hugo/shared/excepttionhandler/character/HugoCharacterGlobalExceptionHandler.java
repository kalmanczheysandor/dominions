package hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.excepttionhandler.character;


import hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.exception.character.*;

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
    public GeneralFailureResponse handleCharacterNotFound(HugoCharacterNotFoundException ex) {

        log.error("HugoCharacter not found: {}", ex.getMessage(), ex);

        // Generate output
        GeneralFailureResponse response = new GeneralFailureResponse(
                ex.getClass().getSimpleName(),
                ex.getMessage()
        );

        return response;
    }



    @ExceptionHandler(HugoCharacterNotFoundByUuidException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public GeneralFailureResponse handleCharacterNotFoundByUuid(HugoCharacterNotFoundByUuidException ex) {

        log.error("HugoCharacter not found by UUID: {}", ex.getUuid(), ex);

        // Generate output
        GeneralFailureResponse response = new GeneralFailureResponse(
                ex.getClass().getSimpleName(),
                ex.getMessage()
        );
        response.addParameter("uuid", ex.getUuid());

        return response;
    }



    @ExceptionHandler(HugoCharacterNotFoundByCodeException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public GeneralFailureResponse handleCharacterNotFoundByCode(HugoCharacterNotFoundByCodeException ex) {

        log.error("HugoCharacter not found by code: {}", ex.getCode(), ex);

        // Generate output
        GeneralFailureResponse response = new GeneralFailureResponse(
                ex.getClass().getSimpleName(),
                ex.getMessage()
        );
        response.addParameter("code", ex.getCode());

        return response;
    }





    @ExceptionHandler(HugoCharacterAssociationRestrictedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GeneralFailureResponse handleCharacterAssociationRestricted(HugoCharacterAssociationRestrictedException ex) {

        log.warn("HugoCharacter association is restricted. Id: {}. Title: {}", ex.getId(), ex.getTitle(), ex);

        // Generate output
        GeneralFailureResponse response = new GeneralFailureResponse(
                ex.getClass().getSimpleName(),
                ex.getMessage()
        );
        response.addParameter("name", ex.getTitle());

        return response;
    }

    @ExceptionHandler(HugoCharacterReferencedElsewhereException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GeneralFailureResponse handleCharacterReferencedElsewhere(HugoCharacterReferencedElsewhereException ex) {

        log.warn("HugoCharacter with id '{}' is still referenced. Name: {}", ex.getId(), ex.getName(), ex);

        // Generate output
        GeneralFailureResponse response = new GeneralFailureResponse(
                ex.getClass().getSimpleName(),
                ex.getMessage()
        );
        response.addParameter("name", ex.getName());

        return response;
    }

    @ExceptionHandler(HugoCharacterNameIsReservedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GeneralFailureResponse handleReservedCharacterName(HugoCharacterNameIsReservedException ex) {

        log.warn("HugoCharacter name is reserved: {}", ex.getName(), ex);

        // Generate output
        GeneralFailureResponse response = new GeneralFailureResponse(
                ex.getClass().getSimpleName(),
                ex.getMessage()
        );
        response.addParameter("name", ex.getName());

        return response;
    }


    @ExceptionHandler(HugoCharacterCodeIsReservedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GeneralFailureResponse handleCharacterCodeIsReserved(HugoCharacterCodeIsReservedException ex) {

        log.warn("HugoCharacter code is reserved: {}", ex.getCode(), ex);

        // Generate output
        GeneralFailureResponse response = new GeneralFailureResponse(
                ex.getClass().getSimpleName(),
                ex.getMessage()
        );
        response.addParameter("code", ex.getCode());

        return response;
    }







}
