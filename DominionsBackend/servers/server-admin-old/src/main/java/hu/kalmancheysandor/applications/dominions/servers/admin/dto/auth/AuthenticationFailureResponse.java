package hu.kalmancheysandor.applications.dominions.servers.admin.dto.auth;

import hu.kalmancheysandor.applications.dominions.servers.admin.dto.failure.TFailureResponse;
import lombok.Data;

@Data
public class AuthenticationFailureResponse extends TFailureResponse {
    private String message;
    public AuthenticationFailureResponse(String message) {
        this.message = message;
    }
}

