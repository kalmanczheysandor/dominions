package hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.record;

import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.CustomException;

public abstract class ParentRecordNotFoundByUuidException extends CustomException {
    private String uuid;

    public ParentRecordNotFoundByUuidException(String uuid) {
        super("ParentRecordNotFound");
        this.uuid = uuid;
    }

    public final String getUuid() {
        return uuid;
    }

    @Override
    public String toString() {
        return "ParentRecordNotFoundByUuidException{" +
            "uuid='" + uuid + '\'' +
            '}';
    }
}
