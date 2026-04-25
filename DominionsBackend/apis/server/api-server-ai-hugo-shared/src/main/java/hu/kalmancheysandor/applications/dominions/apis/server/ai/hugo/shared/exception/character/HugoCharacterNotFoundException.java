package hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.exception.character;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record.RecordNotFoundByIdException;

public class HugoCharacterNotFoundException extends RecordNotFoundByIdException {
    public HugoCharacterNotFoundException(int id) {
        super(id);
    }
}
