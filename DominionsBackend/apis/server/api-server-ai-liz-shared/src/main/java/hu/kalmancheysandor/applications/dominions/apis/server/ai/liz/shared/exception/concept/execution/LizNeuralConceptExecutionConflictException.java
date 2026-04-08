package hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.exception.concept.execution;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.CustomException;

public class LizNeuralConceptExecutionConflictException extends CustomException {

    private int id;
    public LizNeuralConceptExecutionConflictException(int id) {
        super("LizNeuralConceptExecutionConflict");
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
