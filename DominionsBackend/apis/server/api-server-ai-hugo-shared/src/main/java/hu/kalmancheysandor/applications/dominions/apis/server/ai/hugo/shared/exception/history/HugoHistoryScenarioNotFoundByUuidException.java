package hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.exception.history;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record.RecordNotFoundByUuidException;

public class HugoHistoryScenarioNotFoundByUuidException extends RecordNotFoundByUuidException {
    public HugoHistoryScenarioNotFoundByUuidException(String uuid) {
        super(uuid);
    }
}
