package hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.exception.variant;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record.RecordNotFoundByIdException;

public class LizVariantNotFoundException extends RecordNotFoundByIdException {
    public LizVariantNotFoundException(int id) {
        super(id);
    }
    public LizVariantNotFoundException() {
        super();
    }
}
