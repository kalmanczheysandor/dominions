package hu.kalmancheysandor.applications.dominions.apis.server.common.dto.failure;


public class GeneralFailureResponse extends TBasicFailureResponse {
    public GeneralFailureResponse(String code) {
        super(GeneralFailureResponse.class.getSimpleName(),code);
    }

    public GeneralFailureResponse(String code, String message) {
        super(GeneralFailureResponse.class.getSimpleName(),code,message);
    }


}
