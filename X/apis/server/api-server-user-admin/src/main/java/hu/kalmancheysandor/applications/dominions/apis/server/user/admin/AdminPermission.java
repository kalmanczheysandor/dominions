package hu.kalmancheysandor.applications.dominions.apis.server.user.admin;


import hu.kalmancheysandor.applications.dominions.apis.server.user.common.security.TSecurityPermission;

public class AdminPermission extends TSecurityPermission {

    public AdminPermission(String permission) {
        super(permission);
    }

    public static AdminPathSection generator() {
        return new AdminPathSection();
    }

    @Override
    public String toString() {
        return "AdminPermission{" +
                "   permission=" + getAuthority() +
                "}";
    }

}
