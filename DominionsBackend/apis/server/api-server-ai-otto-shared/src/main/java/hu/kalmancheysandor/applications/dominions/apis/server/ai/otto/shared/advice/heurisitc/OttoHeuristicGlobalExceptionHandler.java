package hu.kalmancheysandor.applications.dominions.apis.server.ai.otto.shared.advice.heurisitc;


import hu.kalmancheysandor.applications.dominions.apis.server.ai.otto.shared.exception.evaluator.*;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.otto.shared.exception.evaluator.evaluator.OttoUnexpectedOperatorScoreEvaluatorTypeCodeException;
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
public class OttoHeuristicGlobalExceptionHandler {

    @ExceptionHandler(OttoEvaluatorNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public GeneralFailureResponse handle(OttoEvaluatorNotFoundException ex) {

        log.error("OttoHeuristic not found: {}", ex.getMessage(), ex);

        // Generate output
        GeneralFailureResponse response = new GeneralFailureResponse(
                ex.getClass().getSimpleName(),
                ex.getMessage()
        );

        return response;
    }

    @ExceptionHandler(OttoEvaluatorNotFoundByUuidException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public GeneralFailureResponse handle(OttoEvaluatorNotFoundByUuidException ex) {

        log.error("OttoHeuristic not found by UUID: {}", ex.getUuid(), ex);

        // Generate output
        GeneralFailureResponse response = new GeneralFailureResponse(
                ex.getClass().getSimpleName(),
                ex.getMessage()
        );
        response.addParameter("uuid", ex.getUuid());

        return response;
    }

    @ExceptionHandler(OttoEvaluatorNotFoundByCodeException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public GeneralFailureResponse handle(OttoEvaluatorNotFoundByCodeException ex) {

        log.error("OttoHeuristic not found by code: {}", ex.getCode(), ex);

        // Generate output
        GeneralFailureResponse response = new GeneralFailureResponse(
                ex.getClass().getSimpleName(),
                ex.getMessage()
        );
        response.addParameter("code", ex.getCode());

        return response;
    }

    @ExceptionHandler(OttoEvaluatorAssociationRestrictedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GeneralFailureResponse handle(OttoEvaluatorAssociationRestrictedException ex) {

        log.warn("OttoHeuristic association is restricted. Id: {}. Title: {}", ex.getId(), ex.getTitle(), ex);

        // Generate output
        GeneralFailureResponse response = new GeneralFailureResponse(
                ex.getClass().getSimpleName(),
                ex.getMessage()
        );
        response.addParameter("name", ex.getTitle());

        return response;
    }

    @ExceptionHandler(OttoEvaluatorReferencedElsewhereException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GeneralFailureResponse handle(OttoEvaluatorReferencedElsewhereException ex) {

        log.warn("OttoHeuristic with id '{}' is still referenced. Name: {}", ex.getId(), ex.getName(), ex);

        // Generate output
        GeneralFailureResponse response = new GeneralFailureResponse(
                ex.getClass().getSimpleName(),
                ex.getMessage()
        );
        response.addParameter("id", ex.getId());
        response.addParameter("name", ex.getName());

        return response;
    }

    @ExceptionHandler(OttoEvaluatorNameIsReservedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GeneralFailureResponse handle(OttoEvaluatorNameIsReservedException ex) {

        log.warn("OttoHeuristic name is reserved: {}", ex.getName(), ex);

        // Generate output
        GeneralFailureResponse response = new GeneralFailureResponse(
                ex.getClass().getSimpleName(),
                ex.getMessage()
        );
        response.addParameter("name", ex.getName());

        return response;
    }

    @ExceptionHandler(OttoUnexpectedOperatorScoreEvaluatorTypeCodeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public GeneralFailureResponse handle(OttoUnexpectedOperatorScoreEvaluatorTypeCodeException ex) {

        log.warn("Unexpected OttoHeuristic typeCode: {}", ex.getTypeCode(), ex);

        GeneralFailureResponse response = new GeneralFailureResponse(
                ex.getClass().getSimpleName(),
                ex.getMessage()
        );

        response.addParameter("typeCode", ex.getTypeCode());

        return response;
    }


}
