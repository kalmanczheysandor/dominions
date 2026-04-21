package hu.kalmancheysandor.applications.dominions.servers.ai.hugo.operation.exceptionhandling.entity.heurisitc;


import hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.exception.heuristic.*;
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
public class HugoHeuristicGlobalExceptionHandler {

    @ExceptionHandler(HugoHeuristicNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public GeneralFailureResponse handle(HugoHeuristicNotFoundException exception) {
        // Logging
        log.error("HugoHeuristic not found: {}", exception.getMessage());

        // Generate response
        GeneralFailureResponse response = new GeneralFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());

        return response;
    }

    @ExceptionHandler(HugoHeuristicNotFoundByUuidException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public GeneralFailureResponse handle(HugoHeuristicNotFoundByUuidException exception) {
        // Logging
        log.error("HugoHeuristic not found by UUID: {}", exception.getUuid(), exception);

        // Generate response
        GeneralFailureResponse response = new GeneralFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());
        response.addParameter("uuid", exception.getUuid());

        return response;
    }

    @ExceptionHandler(HugoHeuristicNotFoundByCodeException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public GeneralFailureResponse handle(HugoHeuristicNotFoundByCodeException exception) {
        // Logging
        log.error("HugoHeuristic not found by code: {}", exception.getCode(), exception);

        // Generate response
        GeneralFailureResponse response = new GeneralFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());
        response.addParameter("code", exception.getCode());

        return response;
    }


    @ExceptionHandler(HugoHeuristicAssociationRestrictedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GeneralFailureResponse handle(HugoHeuristicAssociationRestrictedException exception) {
        // Logging
        log.warn("HugoHeuristic association is restricted. HugoHeuristic id: {}. HugoHeuristic title: {}",exception.getId(), exception.getTitle());

        // Generate response
        GeneralFailureResponse response = new GeneralFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());
        response.addParameter("name", exception.getTitle());

        return response;
    }

    @ExceptionHandler(HugoHeuristicReferencedElsewhereException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GeneralFailureResponse handle(HugoHeuristicReferencedElsewhereException exception) {
        // Logging
        log.warn("HugoHeuristic with '{}' id is still referenced with. HugoHeuristic name: {}.", exception.getId(), exception.getName());

        // Generate response
        GeneralFailureResponse response = new GeneralFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());
        response.addParameter("name", exception.getName());

        return response;
    }

    @ExceptionHandler(HugoHeuristicNameIsReservedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GeneralFailureResponse handle(HugoHeuristicNameIsReservedException exception) {
        // Logging
        log.warn("HugoHeuristic name is reserved: {}", exception.getName());

        // Generate response
        GeneralFailureResponse response = new GeneralFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());
        response.addParameter("name", exception.getName());

        return response;
    }



}
