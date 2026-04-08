package hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.exception.cc;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record.RecordNotFoundByUuidException;

public class LizPersonnelSolutionTrainingNotFoundByUuidException extends RecordNotFoundByUuidException {
    public LizPersonnelSolutionTrainingNotFoundByUuidException(String uuid) {
        super(uuid);
    }
}
