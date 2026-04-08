package hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.exception.concept.execution;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.CustomException;

public class LizNeuralConceptNoActiveExecutionFoundException extends CustomException {

    private int id;
    public LizNeuralConceptNoActiveExecutionFoundException(int id) {
        super("LizNeuralConceptNoActiveExecutionFound");
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
