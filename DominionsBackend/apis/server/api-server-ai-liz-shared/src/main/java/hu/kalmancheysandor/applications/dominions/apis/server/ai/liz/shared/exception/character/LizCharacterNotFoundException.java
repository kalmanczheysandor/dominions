package hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.exception.character;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record.RecordNotFoundByIdException;

public class LizCharacterNotFoundException extends RecordNotFoundByIdException {
    public LizCharacterNotFoundException(int id) {
        super(id);
    }
}
