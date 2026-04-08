package hu.kalmancheysandor.applications.dominions.servers.admin.configuration.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


public class SecurityUserDetails implements UserDetails {

    private int id;
    private String username;
    private String password;
    private boolean enabled;
    private Set<? extends GrantedAuthority> authorities = new HashSet<>();

    public SecurityUserDetails() {

    }

    public SecurityUserDetails(String username, Set<? extends GrantedAuthority> authorities) {
        this.username = username;
        this.authorities = authorities;
        //this.password = "$2a$10$a6Cv69DszWPhwY4ya9d7VuvJqvitNTACR7wBy6x0CqP7tSYI1Tgmi";
    }

    public void setAuthorities(Set<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
