package hu.kalmancheysandor.applications.dominions.servers.ai.liz.operation.exceptionhandling.entity.training;


import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.exception.cc.*;
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
public class LizPersonnelSolutionTrainingGlobalExceptionHandler {

    @ExceptionHandler(LizPersonnelSolutionTrainingNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public GeneralFailureResponse handle(LizPersonnelSolutionTrainingNotFoundException exception) {
        // Logging
        log.error("LizPersonnelSolutionTraining not found: {}", exception.getMessage());

        // Generate response
        GeneralFailureResponse response = new GeneralFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());

        return response;
    }

    @ExceptionHandler(LizPersonnelSolutionTrainingNotFoundByUuidException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public GeneralFailureResponse handle(LizPersonnelSolutionTrainingNotFoundByUuidException exception) {
        // Logging
        log.error("LizPersonnelSolutionTraining not found by UUID: {}", exception.getUuid(), exception);

        // Generate response
        GeneralFailureResponse response = new GeneralFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());
        response.addParameter("uuid", exception.getUuid());

        return response;
    }

    @ExceptionHandler(LizPersonnelSolutionTrainingAssociationRestrictedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GeneralFailureResponse handle(LizPersonnelSolutionTrainingAssociationRestrictedException exception) {
        // Logging
        log.warn("LizPersonnelSolutionTraining association is restricted. LizPersonnelSolutionTraining id: {}. LizPersonnelSolutionTraining title: {}",exception.getId(), exception.getTitle());

        // Generate response
        GeneralFailureResponse response = new GeneralFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());
        response.addParameter("title", exception.getTitle());

        return response;
    }

    @ExceptionHandler(LizPersonnelSolutionTrainingReferencedElsewhereException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GeneralFailureResponse handle(LizPersonnelSolutionTrainingReferencedElsewhereException exception) {
        // Logging
        log.warn("LizPersonnelSolutionTraining with '{}' id is still referenced with. LizPersonnelSolutionTraining title: {}.", exception.getId(), exception.getTitle());

        // Generate response
        GeneralFailureResponse response = new GeneralFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());
        response.addParameter("title", exception.getTitle());

        return response;
    }

    @ExceptionHandler(LizPersonnelSolutionTrainingTitleIsReservedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GeneralFailureResponse handle(LizPersonnelSolutionTrainingTitleIsReservedException exception) {
        // Logging
        log.warn("LizPersonnelSolutionTraining title is reserved: {}", exception.getTitle());

        // Generate response
        GeneralFailureResponse response = new GeneralFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());
        response.addParameter("title", exception.getTitle());

        return response;
    }


    @ExceptionHandler(LizPersonnelSolutionTrainingIsNotAChildOfThisParentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GeneralFailureResponse handle(LizPersonnelSolutionTrainingIsNotAChildOfThisParentException exception) {
        // Logging
        log.warn("LizPersonnelSolutionTraining is not a child of referenced parent");

        // Generate response
        GeneralFailureResponse response = new GeneralFailureResponse(exception.getClass().getSimpleName(), exception.getMessage());

        return response;
    }


}
