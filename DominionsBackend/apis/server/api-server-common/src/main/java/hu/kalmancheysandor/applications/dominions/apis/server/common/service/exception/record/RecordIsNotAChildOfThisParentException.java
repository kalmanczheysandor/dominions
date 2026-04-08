package hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record;

import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.CustomException;

public abstract class RecordIsNotAChildOfThisParentException extends CustomException {

    public RecordIsNotAChildOfThisParentException() {
        super("RecordIsNotAChildOfParentException");

    }

}
