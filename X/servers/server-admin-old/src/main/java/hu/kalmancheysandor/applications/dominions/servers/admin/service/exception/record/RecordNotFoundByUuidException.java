package hu.kalmancheysandor.applications.dominions.service.exception.record;

import hu.kalmancheysandor.applications.dominions.service.exception.CustomException;
import hu.kalmancheysandor.applications.dominions.service.exception.CustomException;
import hu.kalmancheysandor.applications.dominions.service.exception.CustomException;

public abstract class RecordNotFoundByUuidException extends CustomException {
    private String uuid;

    public RecordNotFoundByUuidException(String uuid) {
        super("RecordNotFound");
        this.uuid = uuid;
    }

    public final String getUuid() {
        return uuid;
    }

    @Override
    public String toString() {
        return "RecordNotFoundByUuidException{" +
            "uuid='" + uuid + '\'' +
            '}';
    }
}
