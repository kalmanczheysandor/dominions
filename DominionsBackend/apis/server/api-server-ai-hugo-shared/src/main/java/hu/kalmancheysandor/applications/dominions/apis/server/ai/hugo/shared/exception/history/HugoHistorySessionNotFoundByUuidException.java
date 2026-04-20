package hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.exception.history;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record.RecordNotFoundByUuidException;

public class HugoHistorySessionNotFoundByUuidException extends RecordNotFoundByUuidException {
    public HugoHistorySessionNotFoundByUuidException(String uuid) {
        super(uuid);
    }
}
