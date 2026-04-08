package hu.kalmancheysandor.applications.dominions.apis.util.uuid;

public class InvalidUuidStringException extends RuntimeException {
    private String uuid;

    public InvalidUuidStringException(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }

    @Override
    public String toString() {
        return "InvalidUuidStringException{" +
            "uuid='" + uuid + '\'' +
            '}';
    }
}
