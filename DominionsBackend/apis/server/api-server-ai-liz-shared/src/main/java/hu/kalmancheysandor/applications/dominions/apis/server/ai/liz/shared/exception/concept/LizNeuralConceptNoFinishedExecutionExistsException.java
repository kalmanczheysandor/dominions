package hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.exception.concept;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.CustomException;

public class LizNeuralConceptNoFinishedExecutionExistsException extends CustomException {
    public LizNeuralConceptNoFinishedExecutionExistsException() {
        super("LizNeuralConceptNoFinishedExecutionExistsException");
    }
}
