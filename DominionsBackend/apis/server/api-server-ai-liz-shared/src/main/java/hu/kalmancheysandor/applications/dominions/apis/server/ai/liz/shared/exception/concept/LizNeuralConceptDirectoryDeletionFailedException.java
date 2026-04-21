package hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.exception.concept;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.CustomException;

public class LizNeuralConceptDirectoryDeletionFailedException extends CustomException {
    private int id;
    public LizNeuralConceptDirectoryDeletionFailedException(int id) {
        super("LizNeuralConceptDirectoryDeletionFailedException");
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
