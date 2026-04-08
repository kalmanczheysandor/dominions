package hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.exception.concept;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record.RecordNotFoundByUuidException;

public class LizNeuralConceptHistoryScenarioNotFoundByUuidException extends RecordNotFoundByUuidException {
    public LizNeuralConceptHistoryScenarioNotFoundByUuidException(String uuid) {
        super(uuid);
    }
}
