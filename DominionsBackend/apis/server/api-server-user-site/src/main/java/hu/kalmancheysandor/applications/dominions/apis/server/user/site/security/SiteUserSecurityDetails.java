package hu.kalmancheysandor.applications.dominions.apis.server.user.site.security;

import hu.kalmancheysandor.applications.dominions.apis.server.user.common.security.UserSecurityDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


public class SiteUserSecurityDetails extends UserSecurityDetails  {
    public SiteUserSecurityDetails() {
    }

    public SiteUserSecurityDetails(String username, Set<? extends GrantedAuthority> authorities) {
        super(username, authorities);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SiteUserSecurityDetails)) return false;
        SiteUserSecurityDetails that = (SiteUserSecurityDetails) o;
        return Objects.equals(this.getUsername(), that.getUsername());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsername());
    }
}
