package hu.kalmancheysandor.applications.dominions.servers.admin.dto.failure;



public class GeneralFailureResponse extends TBasicFailureResponse {
    public GeneralFailureResponse(String code) {
        super(GeneralFailureResponse.class.getSimpleName(),code);
    }

    public GeneralFailureResponse(String code, String message) {
        super(GeneralFailureResponse.class.getSimpleName(),code,message);
    }


}
