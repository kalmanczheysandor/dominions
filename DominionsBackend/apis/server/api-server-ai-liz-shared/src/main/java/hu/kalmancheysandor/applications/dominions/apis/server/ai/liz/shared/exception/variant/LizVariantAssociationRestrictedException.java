package hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.exception.variant;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record.RecordAssociationRestrictedException;

public class LizVariantAssociationRestrictedException extends RecordAssociationRestrictedException {
    private String title;

    public LizVariantAssociationRestrictedException(int id, String title) {
        super(id);
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
