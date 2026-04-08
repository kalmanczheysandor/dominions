package hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.exception.character;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record.RecordNotFoundByUuidException;

public class LizCharacterNotFoundByUuidException extends RecordNotFoundByUuidException {
    public LizCharacterNotFoundByUuidException(String uuid) {
        super(uuid);
    }
}
