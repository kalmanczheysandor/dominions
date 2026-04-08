package hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record;

import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.CustomException;

public abstract class RecordDuplicateConstraintException extends CustomException {
    private String fieldName;
    private String fieldValue;

    public RecordDuplicateConstraintException(String fieldName, String fieldValue) {
        super("RecordDuplicate");
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getFieldValue() {
        return fieldValue;
    }
}
