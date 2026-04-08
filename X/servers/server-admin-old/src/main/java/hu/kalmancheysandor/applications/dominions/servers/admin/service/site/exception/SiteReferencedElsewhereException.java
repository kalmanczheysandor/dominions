package hu.kalmancheysandor.applications.dominions.service.site.exception;

import hu.kalmancheysandor.applications.dominions.service.exception.record.RecordReferencedElsewhereException;
import hu.kalmancheysandor.applications.dominions.service.exception.record.RecordReferencedElsewhereException;
import hu.kalmancheysandor.applications.dominions.service.exception.record.RecordReferencedElsewhereException;

public class SiteReferencedElsewhereException extends RecordReferencedElsewhereException {
    private String name;

    public SiteReferencedElsewhereException(int id, String name) {
        super(id);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
