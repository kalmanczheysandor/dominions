package hu.kalmancheysandor.applications.dominions.servers.admin.exceptionhandling.entity.game.scenario;


import hu.kalmancheysandor.applications.dominions.apis.server.common.dto.failure.GeneralFailureResponse;
import hu.kalmancheysandor.applications.dominions.servers.admin.service.game.scenario.exception.*;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import lombok.extern.slf4j.Slf4j;

@Order(1)
@Slf4j
@RestControllerAdvice
public class GameScenarioGlobalExceptionHandler {

    @ExceptionHandler(GameScenarioNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public GeneralFailureResponse handle(GameScenarioNotFoundException exception) {
        // Logging
        log.error("GameScenario not found: {}", exception.getMessage());

        // Generate response
        GeneralFailureResponse response = new GeneralFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());

        return response;
    }

    @ExceptionHandler(GameScenarioNotFoundByUuidException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public GeneralFailureResponse handle(GameScenarioNotFoundByUuidException exception) {
        // Logging
        log.error("GameScenario not found by UUID: {}", exception.getUuid(), exception);

        // Generate response
        GeneralFailureResponse response = new GeneralFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());
        response.addParameter("uuid", exception.getUuid());

        return response;
    }

    @ExceptionHandler(GameScenarioTitleIsReservedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GeneralFailureResponse handle(GameScenarioTitleIsReservedException exception) {
        // Logging
        log.warn("GameScenario title is reserved: {}", exception.getTitle());

        // Generate response
        GeneralFailureResponse response = new GeneralFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());
        response.addParameter("title", exception.getTitle());

        return response;
    }

    @ExceptionHandler(GameScenarioAssociationRestrictedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GeneralFailureResponse handle(GameScenarioAssociationRestrictedException exception) {
        // Logging
        log.warn("GameScenario association is restricted. GameScenario id: {}. GameScenario title: {}",exception.getId(), exception.getTitle());

        // Generate response
        GeneralFailureResponse response = new GeneralFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());
        response.addParameter("title", exception.getTitle());

        return response;
    }


    @ExceptionHandler(GameScenarioReferencedElsewhereException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GeneralFailureResponse handle(GameScenarioReferencedElsewhereException exception) {
        // Logging
        log.warn("GameScenario with '{}' id is still referenced with. GameScenario title: {}.", exception.getId(), exception.getTitle());

        // Generate response
        GeneralFailureResponse response = new GeneralFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());
        response.addParameter("title", exception.getTitle());

        return response;
    }


}
