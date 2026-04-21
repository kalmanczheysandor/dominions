package hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.exception.concept;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.CustomException;

public class LizNeuralConceptDeletionBlockedByActiveExecutionException extends CustomException {
    private int id;

    public LizNeuralConceptDeletionBlockedByActiveExecutionException(int id) {
        super("LizNeuralConceptDeletionBlockedByActiveExecutionException");
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
