package hu.kalmancheysandor.applications.dominions.servers.admin.dto.auth;

import java.util.List;

public class AuthenticationSuccessResponse {
    private String identifier;
    private List permissions;

    public AuthenticationSuccessResponse(String identifier, List permissions) {
        this.identifier = identifier;
        this.permissions = permissions;
    }

    public String getIdentifier() {
        return identifier;
    }

    public List getPermissions() {
        return permissions;
    }

}

