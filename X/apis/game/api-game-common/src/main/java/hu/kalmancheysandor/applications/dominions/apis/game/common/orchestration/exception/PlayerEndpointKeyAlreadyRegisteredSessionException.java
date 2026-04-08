package hu.kalmancheysandor.applications.dominions.apis.game.common.orchestration.exception;

public class PlayerEndpointKeyAlreadyRegisteredSessionException extends TSessionException {
    private String endpointKey;

    public PlayerEndpointKeyAlreadyRegisteredSessionException(String sessionKey, String endpointKey) {
        super(sessionKey);
        this.endpointKey = endpointKey;
    }

    public String getEndpointKey() {
        return endpointKey;
    }
}
