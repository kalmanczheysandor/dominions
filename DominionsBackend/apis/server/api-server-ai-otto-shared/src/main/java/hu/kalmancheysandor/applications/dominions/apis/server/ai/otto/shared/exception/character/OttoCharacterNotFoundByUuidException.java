package hu.kalmancheysandor.applications.dominions.apis.server.ai.otto.shared.exception.character;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record.RecordNotFoundByUuidException;

public class OttoCharacterNotFoundByUuidException extends RecordNotFoundByUuidException {
    public OttoCharacterNotFoundByUuidException(String uuid) {
        super(uuid);
    }
}
