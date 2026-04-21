package hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.exception.heuristic;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record.RecordNotFoundByIdException;

public class HugoHeuristicNotFoundException extends RecordNotFoundByIdException {
    public HugoHeuristicNotFoundException(int id) {
        super(id);
    }
}
