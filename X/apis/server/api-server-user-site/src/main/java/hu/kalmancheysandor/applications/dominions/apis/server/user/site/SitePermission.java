package hu.kalmancheysandor.applications.dominions.apis.server.user.site;


import hu.kalmancheysandor.applications.dominions.apis.server.user.common.security.TSecurityPermission;

public class SitePermission extends TSecurityPermission {

    public SitePermission(String permission) {
        super(permission);
    }

    public static SitePathSection generator() {
        return new SitePathSection();
    }

    @Override
    public String toString() {
        return "AdminPermission{" +
                "   permission=" + getAuthority() +
                "}";
    }

}
