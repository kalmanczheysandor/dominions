package hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.exception.concept;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record.RecordNotFoundByIdException;

public class LizNeuralConceptNotFoundException extends RecordNotFoundByIdException {
    public LizNeuralConceptNotFoundException(int id) {
        super(id);
    }
    public LizNeuralConceptNotFoundException() {
        super();
    }
}
