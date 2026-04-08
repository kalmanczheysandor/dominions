package hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.exception.cc;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record.RecordNotFoundByIdException;

public class LizPersonnelSolutionTrainingNotFoundException extends RecordNotFoundByIdException {
    public LizPersonnelSolutionTrainingNotFoundException(int id) {
        super(id);
    }
}
