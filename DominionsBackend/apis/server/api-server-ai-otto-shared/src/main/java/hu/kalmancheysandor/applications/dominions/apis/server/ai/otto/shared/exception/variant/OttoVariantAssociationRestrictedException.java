package hu.kalmancheysandor.applications.dominions.apis.server.ai.otto.shared.exception.variant;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record.RecordAssociationRestrictedException;

public class OttoVariantAssociationRestrictedException extends RecordAssociationRestrictedException {
    private String title;

    public OttoVariantAssociationRestrictedException(int id, String title) {
        super(id);
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
