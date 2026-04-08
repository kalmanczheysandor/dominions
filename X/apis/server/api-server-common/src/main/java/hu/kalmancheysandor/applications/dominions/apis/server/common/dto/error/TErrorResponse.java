package hu.kalmancheysandor.applications.dominions.apis.server.common.dto.error;

import java.util.HashMap;
import java.util.Map;

public abstract class TErrorResponse  {
    private String code;
    private String message;
    private Map<String, Object> parameters = new HashMap<>();

    public TErrorResponse() {
    }

    public TErrorResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    public String getMessage() {
        return message;
    }

    protected void setMessage(String message) {
        this.message = message;
    }


    public Map<String, Object> getParameters() {
        return parameters;
    }

    protected void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }

    public void addParameter(String key, Object value) {
        parameters.put(key, value);
    }
}
