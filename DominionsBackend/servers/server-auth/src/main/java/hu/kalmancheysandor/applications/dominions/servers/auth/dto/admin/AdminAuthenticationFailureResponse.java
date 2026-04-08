package hu.kalmancheysandor.applications.dominions.servers.auth.dto.admin;

import hu.kalmancheysandor.applications.dominions.apis.server.common.dto.failure.AuthenticationFailureResponse;
import hu.kalmancheysandor.applications.dominions.apis.server.common.dto.failure.TFailureResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class AdminAuthenticationFailureResponse extends AuthenticationFailureResponse {
    public AdminAuthenticationFailureResponse(String code) {
        super(code);
    }

    public AdminAuthenticationFailureResponse(String code, String message) {
        super(code, message);
    }

    public AdminAuthenticationFailureResponse(String code, String message, String endpoint) {
        super(code, message, endpoint);
    }
}

