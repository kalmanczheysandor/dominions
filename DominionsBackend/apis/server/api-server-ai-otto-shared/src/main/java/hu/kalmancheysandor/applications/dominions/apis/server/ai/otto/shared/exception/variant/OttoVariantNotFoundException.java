package hu.kalmancheysandor.applications.dominions.apis.server.ai.otto.shared.exception.variant;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record.RecordNotFoundByIdException;

public class OttoVariantNotFoundException extends RecordNotFoundByIdException {
    public OttoVariantNotFoundException(int id) {
        super(id);
    }

    public OttoVariantNotFoundException() {
        super();
    }
}
