package hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.advice.heurisitc;


import hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.exception.heuristic.*;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.exception.heuristic.evaluator.HugoUnexpectedHeuristicEvaluatorTypeCodeException;
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
    public GeneralFailureResponse handle(HugoHeuristicNotFoundException ex) {

        log.error("HugoHeuristic not found: {}", ex.getMessage(), ex);

        // Generate output
        GeneralFailureResponse response = new GeneralFailureResponse(
                ex.getClass().getSimpleName(),
                ex.getMessage()
        );

        return response;
    }

    @ExceptionHandler(HugoHeuristicNotFoundByUuidException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public GeneralFailureResponse handle(HugoHeuristicNotFoundByUuidException ex) {

        log.error("HugoHeuristic not found by UUID: {}", ex.getUuid(), ex);

        // Generate output
        GeneralFailureResponse response = new GeneralFailureResponse(
                ex.getClass().getSimpleName(),
                ex.getMessage()
        );
        response.addParameter("uuid", ex.getUuid());

        return response;
    }

    @ExceptionHandler(HugoHeuristicNotFoundByCodeException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public GeneralFailureResponse handle(HugoHeuristicNotFoundByCodeException ex) {

        log.error("HugoHeuristic not found by code: {}", ex.getCode(), ex);

        // Generate output
        GeneralFailureResponse response = new GeneralFailureResponse(
                ex.getClass().getSimpleName(),
                ex.getMessage()
        );
        response.addParameter("code", ex.getCode());

        return response;
    }

    @ExceptionHandler(HugoHeuristicAssociationRestrictedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GeneralFailureResponse handle(HugoHeuristicAssociationRestrictedException ex) {

        log.warn("HugoHeuristic association is restricted. Id: {}. Title: {}", ex.getId(), ex.getTitle(), ex);

        // Generate output
        GeneralFailureResponse response = new GeneralFailureResponse(
                ex.getClass().getSimpleName(),
                ex.getMessage()
        );
        response.addParameter("name", ex.getTitle());

        return response;
    }

    @ExceptionHandler(HugoHeuristicReferencedElsewhereException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GeneralFailureResponse handle(HugoHeuristicReferencedElsewhereException ex) {

        log.warn("HugoHeuristic with id '{}' is still referenced. Name: {}", ex.getId(), ex.getName(), ex);

        // Generate output
        GeneralFailureResponse response = new GeneralFailureResponse(
                ex.getClass().getSimpleName(),
                ex.getMessage()
        );
        response.addParameter("id", ex.getId());
        response.addParameter("name", ex.getName());

        return response;
    }

    @ExceptionHandler(HugoHeuristicNameIsReservedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GeneralFailureResponse handle(HugoHeuristicNameIsReservedException ex) {

        log.warn("HugoHeuristic name is reserved: {}", ex.getName(), ex);

        // Generate output
        GeneralFailureResponse response = new GeneralFailureResponse(
                ex.getClass().getSimpleName(),
                ex.getMessage()
        );
        response.addParameter("name", ex.getName());

        return response;
    }

    @ExceptionHandler(HugoUnexpectedHeuristicEvaluatorTypeCodeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public GeneralFailureResponse handle(HugoUnexpectedHeuristicEvaluatorTypeCodeException ex) {

        log.warn("Unexpected HugoHeuristic typeCode: {}", ex.getTypeCode(), ex);

        GeneralFailureResponse response = new GeneralFailureResponse(
                ex.getClass().getSimpleName(),
                ex.getMessage()
        );

        response.addParameter("typeCode", ex.getTypeCode());

        return response;
    }


}
