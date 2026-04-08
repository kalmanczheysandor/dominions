package hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.exception.variant;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record.RecordNotFoundByUuidException;

public class LizVariantNotFoundByUuidException extends RecordNotFoundByUuidException {
    public LizVariantNotFoundByUuidException(String uuid) {
        super(uuid);
    }
}
