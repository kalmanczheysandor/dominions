package hu.kalmancheysandor.applications.dominions.service.dog.exception;

import hu.kalmancheysandor.applications.dominions.service.exception.record.RecordDuplicateConstraintException;
import hu.kalmancheysandor.applications.dominions.service.exception.record.RecordDuplicateConstraintException;
import hu.kalmancheysandor.applications.dominions.service.exception.record.RecordDuplicateConstraintException;

public class DogPrnIsReservedException extends RecordDuplicateConstraintException {
    private String prn;

    public DogPrnIsReservedException(String prn) {
        super("prn", prn);
        this.prn = prn;
    }

    public String getPrn() {
        return prn;
    }
}
