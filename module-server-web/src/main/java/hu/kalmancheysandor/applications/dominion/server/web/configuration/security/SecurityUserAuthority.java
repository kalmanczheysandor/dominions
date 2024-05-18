package hu.kalmancheysandor.applications.dominion.server.web.configuration.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

@Data
@AllArgsConstructor
public class SecurityUserAuthority implements GrantedAuthority {
    private String authority;
}
