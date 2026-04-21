package hu.kalmancheysandor.applications.dominions.servers.ai.liz.operation.exceptionhandling.entity.concept;


import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.exception.concept.*;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.exception.variant.*;
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
public class LizNeuralConceptGlobalExceptionHandler {

    @ExceptionHandler(LizNeuralConceptNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public GeneralFailureResponse handle(LizNeuralConceptNotFoundException exception) {
        // Logging
        log.error("LizNeuralConcept not found: {}", exception.getMessage());

        // Generate response
        // Response
        GeneralFailureResponse response = new GeneralFailureResponse(
                exception.getClass().getSimpleName(),
                exception.getMessage()
        );
        return response;
    }

    @ExceptionHandler(LizNeuralConceptNotFoundByUuidException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public GeneralFailureResponse handle(LizNeuralConceptNotFoundByUuidException exception) {
        // Logging
        log.error("LizNeuralConcept not found by UUID: {}", exception.getUuid(), exception);

        // Generate response
        // Response
        GeneralFailureResponse response = new GeneralFailureResponse(
                exception.getClass().getSimpleName(),
                exception.getMessage()
        );
        response.addParameter("uuid", exception.getUuid());

        return response;
    }

    @ExceptionHandler(LizNeuralConceptAssociationRestrictedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GeneralFailureResponse handle(LizNeuralConceptAssociationRestrictedException exception) {
        // Logging
        log.warn("LizNeuralConcept association is restricted. LizNeuralConcept id: {}. LizNeuralConcept title: {}", exception.getId(), exception.getTitle());

        // Generate response
        // Response
        GeneralFailureResponse response = new GeneralFailureResponse(
                exception.getClass().getSimpleName(),
                exception.getMessage()
        );
        response.addParameter("name", exception.getTitle());

        return response;
    }

    @ExceptionHandler(LizNeuralConceptReferencedElsewhereException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GeneralFailureResponse handle(LizNeuralConceptReferencedElsewhereException exception) {
        // Logging
        log.warn("LizNeuralConcept with '{}' id is still referenced with. LizNeuralConcept name: {}.", exception.getId(), exception.getName());

        // Generate response
        // Response
        GeneralFailureResponse response = new GeneralFailureResponse(
                exception.getClass().getSimpleName(),
                exception.getMessage()
        );
        response.addParameter("name", exception.getName());

        return response;
    }

    @ExceptionHandler(LizNeuralConceptNameIsReservedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GeneralFailureResponse handle(LizNeuralConceptNameIsReservedException exception) {
        // Logging
        log.warn("LizNeuralConcept name is reserved: {}", exception.getName());

        // Generate response
        // Response
        GeneralFailureResponse response = new GeneralFailureResponse(
                exception.getClass().getSimpleName(),
                exception.getMessage()
        );
        response.addParameter("name", exception.getName());

        return response;
    }


    @ExceptionHandler(LizNeuralConceptHistoryPlayerNotFoundByUuidException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public GeneralFailureResponse handle(LizNeuralConceptHistoryPlayerNotFoundByUuidException exception) {
        // Logging
        log.error("LizNeuralConcept-HistoryPlayer not found by UUID: {}", exception.getUuid(), exception);

        // Generate response
        // Response
        GeneralFailureResponse response = new GeneralFailureResponse(
                exception.getClass().getSimpleName(),
                exception.getMessage()
        );
        response.addParameter("uuid", exception.getUuid());

        return response;
    }

    @ExceptionHandler(LizNeuralConceptHistoryScenarioNotFoundByUuidException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public GeneralFailureResponse handle(LizNeuralConceptHistoryScenarioNotFoundByUuidException exception) {
        // Logging
        log.error("LizNeuralConcept-HistoryScenario not found by UUID: {}", exception.getUuid(), exception);

        // Generate response
        // Response
        GeneralFailureResponse response = new GeneralFailureResponse(
                exception.getClass().getSimpleName(),
                exception.getMessage()
        );
        response.addParameter("uuid", exception.getUuid());

        return response;
    }


    @ExceptionHandler(LizNeuralConceptDirectoryDeletionFailedException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public GeneralFailureResponse handle(LizNeuralConceptDirectoryDeletionFailedException exception) {

        // Logging
        log.error("Failed to delete LizNeuralConcept directory", exception);

        // Generate response
        // Response
        GeneralFailureResponse response = new GeneralFailureResponse(
                exception.getClass().getSimpleName(),
                exception.getMessage()
        );
        response.addParameter("id", exception.getId());
        return response;
    }

    @ExceptionHandler(LizNeuralConceptDeletionBlockedByActiveExecutionException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GeneralFailureResponse handle(LizNeuralConceptDeletionBlockedByActiveExecutionException exception) {

        // Logging
        log.warn("Deletion blocked for LizNeuralConcept {} due to active execution",
                exception.getId(), exception);

        // Response
        GeneralFailureResponse response = new GeneralFailureResponse(
                exception.getClass().getSimpleName(),
                exception.getMessage()
        );

        response.addParameter("id", exception.getId());
        return response;
    }
}
