package hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.exception.history;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record.RecordNotFoundByUuidException;

public class LizHistoryScenarioNotFoundByUuidException extends RecordNotFoundByUuidException {
    public LizHistoryScenarioNotFoundByUuidException(String uuid) {
        super(uuid);
    }
}
