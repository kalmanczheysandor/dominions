package hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.exception.concept.execution;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.CustomException;

public class LizNeuralConceptAlreadyExecutingException extends CustomException {

    private int id;
    public LizNeuralConceptAlreadyExecutingException(int id) {
        super("LizNeuralConceptAlreadyExecuting");
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
