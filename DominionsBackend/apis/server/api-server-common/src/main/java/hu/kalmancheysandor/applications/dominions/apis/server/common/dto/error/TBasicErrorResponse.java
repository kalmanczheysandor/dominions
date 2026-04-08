package hu.kalmancheysandor.applications.dominions.apis.server.common.dto.error;




public abstract class TBasicErrorResponse extends TErrorResponse {
    private final String type;

    public TBasicErrorResponse(String type,String code) {
        setCode(code);
        this.type=type;
    }

    public TBasicErrorResponse(String type, String code, String message) {
        super(code,message);
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
