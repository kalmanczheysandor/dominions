package hu.kalmancheysandor.applications.dominions.apis.server.ai.otto.shared.exception.character;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record.RecordAssociationRestrictedException;

public class OttoCharacterAssociationRestrictedException extends RecordAssociationRestrictedException {
    private String title;

    public OttoCharacterAssociationRestrictedException(int id, String title) {
        super(id);
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
