package hu.kalmancheysandor.applications.dominions.apis.server.ai.otto.shared.exception.history;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record.RecordNotFoundByUuidException;

public class OttoHistoryScenarioNotFoundByUuidException extends RecordNotFoundByUuidException {
    public OttoHistoryScenarioNotFoundByUuidException(String uuid) {
        super(uuid);
    }
}
