package hu.kalmancheysandor.applications.dominions.apis.server.common.dto.error;


public class GeneralErrorResponse extends TBasicErrorResponse {
    public GeneralErrorResponse(String code) {
        super(GeneralErrorResponse.class.getSimpleName(),code);
    }

    public GeneralErrorResponse(String code, String message) {
        super(GeneralErrorResponse.class.getSimpleName(),code,message);
    }
}
