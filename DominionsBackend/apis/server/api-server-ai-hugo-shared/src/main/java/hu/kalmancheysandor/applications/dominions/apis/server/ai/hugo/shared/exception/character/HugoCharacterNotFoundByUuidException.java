package hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.exception.character;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record.RecordNotFoundByUuidException;

public class HugoCharacterNotFoundByUuidException extends RecordNotFoundByUuidException {
    public HugoCharacterNotFoundByUuidException(String uuid) {
        super(uuid);
    }
}
