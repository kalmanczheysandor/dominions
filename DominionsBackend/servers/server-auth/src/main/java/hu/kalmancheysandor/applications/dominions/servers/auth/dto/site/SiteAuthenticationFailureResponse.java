package hu.kalmancheysandor.applications.dominions.servers.auth.dto.site;

import hu.kalmancheysandor.applications.dominions.apis.server.common.dto.failure.AuthenticationFailureResponse;


public class SiteAuthenticationFailureResponse extends AuthenticationFailureResponse {
    public SiteAuthenticationFailureResponse(String code) {
        super(code);
    }

    public SiteAuthenticationFailureResponse(String code, String message) {
        super(code, message);
    }

    public SiteAuthenticationFailureResponse(String code, String message, String endpoint) {
        super(code, message, endpoint);
    }
}

