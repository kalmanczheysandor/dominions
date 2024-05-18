package hu.kalmanczheysandor.applications.dominion.configuration.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


public class SecurityUser implements UserDetails {

    private String username;
    private String password;
    private Set<? extends GrantedAuthority> authorities = new HashSet<>();

    public SecurityUser() {

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
        return true;
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


//
//  public CustomSecurityUser(User user) {
//    this.setAuthorities(user.getAuthorities());
//    this.setId(user.getId());
//    this.setName(user.getName());
//    this.setPassword(user.getPassword());
//    this.setUsername(user.getUsername());
//  }

}
