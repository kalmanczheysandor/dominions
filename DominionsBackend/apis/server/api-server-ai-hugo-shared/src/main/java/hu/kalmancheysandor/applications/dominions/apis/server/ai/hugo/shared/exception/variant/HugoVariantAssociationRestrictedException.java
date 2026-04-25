package hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.exception.variant;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record.RecordAssociationRestrictedException;

public class HugoVariantAssociationRestrictedException extends RecordAssociationRestrictedException {
    private String title;

    public HugoVariantAssociationRestrictedException(int id, String title) {
        super(id);
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
