package hu.kalmancheysandor.application.dominion.server.game.exceptionhandling;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDetails {
    private String type;
    private String caseCode;

    private Map<String, Object> parameters=new HashMap<>();


    public ErrorDetails(String type) {
        this.type = type;
    }


    public ErrorDetails(Map<String, Object> parameters, String type) {
        this.parameters = parameters;
        this.type = type;
    }
}
