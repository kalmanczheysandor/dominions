package hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.exception.concept;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record.RecordNotFoundByUuidException;

public class LizNeuralConceptNotFoundByUuidException extends RecordNotFoundByUuidException {
    public LizNeuralConceptNotFoundByUuidException(String uuid) {
        super(uuid);
    }
}
