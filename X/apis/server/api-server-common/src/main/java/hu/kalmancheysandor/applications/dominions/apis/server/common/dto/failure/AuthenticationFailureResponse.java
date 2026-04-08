package hu.kalmancheysandor.applications.dominions.apis.server.common.dto.failure;


public class AuthenticationFailureResponse extends TBasicFailureResponse {
    private String endpoint;

    public AuthenticationFailureResponse(String code) {
        super(AuthenticationFailureResponse.class.getSimpleName(),code);
    }

    public AuthenticationFailureResponse(String code, String message) {
        super(AuthenticationFailureResponse.class.getSimpleName(),code,message);
    }

    public AuthenticationFailureResponse(String code, String message, String endpoint) {
        super(AuthenticationFailureResponse.class.getSimpleName(),code,message);
        this.endpoint = endpoint;
    }

    public String getEndpoint() {
        return endpoint;
    }
}
