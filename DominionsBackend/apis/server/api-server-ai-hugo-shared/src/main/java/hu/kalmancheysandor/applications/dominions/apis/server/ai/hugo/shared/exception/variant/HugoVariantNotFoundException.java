package hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.exception.variant;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record.RecordNotFoundByIdException;

public class HugoVariantNotFoundException extends RecordNotFoundByIdException {
    public HugoVariantNotFoundException(int id) {
        super(id);
    }
    public HugoVariantNotFoundException() {
        super();
    }
}
