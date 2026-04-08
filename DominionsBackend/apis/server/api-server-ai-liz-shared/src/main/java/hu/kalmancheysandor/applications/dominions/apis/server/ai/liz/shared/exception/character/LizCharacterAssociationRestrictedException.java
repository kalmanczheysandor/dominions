package hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.exception.character;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record.RecordAssociationRestrictedException;

public class LizCharacterAssociationRestrictedException extends RecordAssociationRestrictedException {
    private String title;

    public LizCharacterAssociationRestrictedException(int id, String title) {
        super(id);
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
