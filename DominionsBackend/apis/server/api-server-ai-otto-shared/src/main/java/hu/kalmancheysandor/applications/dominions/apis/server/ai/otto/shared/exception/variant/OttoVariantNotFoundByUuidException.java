package hu.kalmancheysandor.applications.dominions.apis.server.ai.otto.shared.exception.variant;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record.RecordNotFoundByUuidException;

public class OttoVariantNotFoundByUuidException extends RecordNotFoundByUuidException {
    public OttoVariantNotFoundByUuidException(String uuid) {
        super(uuid);
    }
}
