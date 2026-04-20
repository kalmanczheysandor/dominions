package hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.exception.variant;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record.RecordNotFoundByUuidException;

public class HugoVariantNotFoundByUuidException extends RecordNotFoundByUuidException {
    public HugoVariantNotFoundByUuidException(String uuid) {
        super(uuid);
    }
}
