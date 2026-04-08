package hu.kalmancheysandor.applications.dominions.service.site.exception;

import hu.kalmancheysandor.applications.dominions.service.exception.record.RecordAssociationRestrictedException;
import hu.kalmancheysandor.applications.dominions.service.exception.record.RecordAssociationRestrictedException;
import hu.kalmancheysandor.applications.dominions.service.exception.record.RecordAssociationRestrictedException;

public class SiteAssociationRestrictedException extends RecordAssociationRestrictedException {
    private String name;

    public SiteAssociationRestrictedException(int id, String name) {
        super(id);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
