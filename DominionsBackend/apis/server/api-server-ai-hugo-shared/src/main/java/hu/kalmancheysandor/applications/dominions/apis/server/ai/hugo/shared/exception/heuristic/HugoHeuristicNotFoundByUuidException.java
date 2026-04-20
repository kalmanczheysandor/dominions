package hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.exception.heuristic;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record.RecordNotFoundByUuidException;

public class HugoHeuristicNotFoundByUuidException extends RecordNotFoundByUuidException {
    public HugoHeuristicNotFoundByUuidException(String uuid) {
        super(uuid);
    }
}
