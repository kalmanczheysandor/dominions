package hu.kalmancheysandor.applications.dominions.apis.server.ai.otto.shared.exception.character;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record.RecordNotFoundByIdException;

public class OttoCharacterNotFoundException extends RecordNotFoundByIdException {
    public OttoCharacterNotFoundException(int id) {
        super(id);
    }
}
