package hu.kalmancheysandor.applications.dominions.servers.ai.liz.operation.exceptionhandling.entity.solution;


import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.exception.solution.*;
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
public class LizPersonnelSolutionGlobalExceptionHandler {

    @ExceptionHandler(LizPersonnelSolutionNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public GeneralFailureResponse handle(LizPersonnelSolutionNotFoundException exception) {
        // Logging
        log.error("LizPersonnelSolution not found: {}", exception.getMessage());

        // Generate response
        GeneralFailureResponse response = new GeneralFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());

        return response;
    }

    @ExceptionHandler(LizPersonnelSolutionNotFoundByUuidException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public GeneralFailureResponse handle(LizPersonnelSolutionNotFoundByUuidException exception) {
        // Logging
        log.error("LizPersonnelSolution not found by UUID: {}", exception.getUuid(), exception);

        // Generate response
        GeneralFailureResponse response = new GeneralFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());
        response.addParameter("uuid", exception.getUuid());

        return response;
    }

    @ExceptionHandler(LizPersonnelSolutionAssociationRestrictedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GeneralFailureResponse handle(LizPersonnelSolutionAssociationRestrictedException exception) {
        // Logging
        log.warn("LizPersonnelSolution association is restricted. LizPersonnelSolution id: {}. LizPersonnelSolution title: {}",exception.getId(), exception.getTitle());

        // Generate response
        GeneralFailureResponse response = new GeneralFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());
        response.addParameter("title", exception.getTitle());

        return response;
    }

    @ExceptionHandler(LizPersonnelSolutionReferencedElsewhereException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GeneralFailureResponse handle(LizPersonnelSolutionReferencedElsewhereException exception) {
        // Logging
        log.warn("LizPersonnelSolution with '{}' id is still referenced with. LizPersonnelSolution name: {}.", exception.getId(), exception.getTitle());

        // Generate response
        GeneralFailureResponse response = new GeneralFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());
        response.addParameter("title", exception.getTitle());

        return response;
    }

    @ExceptionHandler(LizPersonnelSolutionTitleIsReservedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GeneralFailureResponse handle(LizPersonnelSolutionTitleIsReservedException exception) {
        // Logging
        log.warn("LizPersonnelSolution title is reserved: {}", exception.getTitle());

        // Generate response
        GeneralFailureResponse response = new GeneralFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());
        response.addParameter("title", exception.getTitle());

        return response;
    }


    @ExceptionHandler(LizPersonnelSolutionIsNotAChildOfThisParentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GeneralFailureResponse handle(LizPersonnelSolutionIsNotAChildOfThisParentException exception) {
        // Logging
        log.warn("LizPersonnelSolution is not a child of referenced parent");

        // Generate response
        GeneralFailureResponse response = new GeneralFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());

        return response;
    }


}
