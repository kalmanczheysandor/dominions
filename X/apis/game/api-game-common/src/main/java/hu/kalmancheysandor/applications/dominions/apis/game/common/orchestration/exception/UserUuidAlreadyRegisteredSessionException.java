package hu.kalmancheysandor.applications.dominions.apis.game.common.orchestration.exception;

public class UserUuidAlreadyRegisteredSessionException extends TSessionException {
    private String userUuid;

    public UserUuidAlreadyRegisteredSessionException(String sessionKey, String userUuid) {
        super(sessionKey);
        this.userUuid = userUuid;
    }

    public String getUserUuid() {
        return userUuid;
    }
}
