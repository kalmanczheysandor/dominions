package hu.kalmanczheysandor.applications.dominion.configuration.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

@Data
@AllArgsConstructor
public class SecurityUserAuthority implements GrantedAuthority {
    private String authority;
}
