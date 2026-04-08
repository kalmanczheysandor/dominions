package hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.exception.solution;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record.RecordNotFoundByIdException;

public class LizPersonnelSolutionNotFoundException extends RecordNotFoundByIdException {
    public LizPersonnelSolutionNotFoundException(int id) {
        super(id);
    }
}
