package hu.kalmancheysandor.applications.dominions.servers.admin.dto.failure;



public abstract class TBasicFailureResponse extends TFailureResponse {
    private final String type;

    public TBasicFailureResponse(String type,String code) {
        setCode(code);
        this.type=type;
    }

    public TBasicFailureResponse(String type, String code, String message) {
        super(code,message);
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
