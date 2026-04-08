package hu.kalmancheysandor.applications.dominions.apis.server.user.common.security;

import org.springframework.security.core.GrantedAuthority;

public abstract class TSecurityPermission implements GrantedAuthority {

    private final String permission;

    public TSecurityPermission(String permission) {
        this.permission = permission;
    }

    @Override
    public String getAuthority() {
        return permission;
    }


    @Override
    public String toString() {
        return "SecurityPermission{" +
                "permission='" + permission + '\'' +
                '}';
    }


}
