package hu.kalmancheysandor.applications.dominions.servers.admin.dto.failure;



public class SecurityFailureResponse extends TBasicFailureResponse {

    public SecurityFailureResponse(String code) {
        super(SecurityFailureResponse.class.getSimpleName(),code);
    }

    public SecurityFailureResponse(String code, String message) {
        super(SecurityFailureResponse.class.getSimpleName(),code,message);
    }

}
