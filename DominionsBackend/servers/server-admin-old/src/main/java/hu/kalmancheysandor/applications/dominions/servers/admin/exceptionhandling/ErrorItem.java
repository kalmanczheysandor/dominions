package hu.kalmancheysandor.applications.dominions.servers.admin.exceptionhandling;


import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class ErrorItem {
    private String code;
    private String message;
    private Map<String, Object> parameters=new HashMap<>();

    public ErrorItem(String code) {
        this.code = code;
    }

    public ErrorItem(String code,String message) {
        this.message = message;
        this.code = code;
    }

    public void addParameter(String key, Object value) {
        parameters.put(key, value);
    }
}
