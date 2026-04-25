package hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.exception.concept.execution;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record.RecordNotFoundByIdException;

public class LizNeuralConceptExecutionNotFoundException extends RecordNotFoundByIdException {
    public LizNeuralConceptExecutionNotFoundException(int id) {
        super(id);
    }
    public LizNeuralConceptExecutionNotFoundException() {
        super();
    }
}
