package hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.exception.solution;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record.RecordNotFoundByUuidException;

public class LizPersonnelSolutionNotFoundByUuidException extends RecordNotFoundByUuidException {
    public LizPersonnelSolutionNotFoundByUuidException(String uuid) {
        super(uuid);
    }
}
