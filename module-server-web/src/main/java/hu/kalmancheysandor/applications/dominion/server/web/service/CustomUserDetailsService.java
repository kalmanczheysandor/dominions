package hu.kalmancheysandor.applications.dominion.server.web.service;

import hu.kalmancheysandor.applications.dominion.server.web.entity.User;
import hu.kalmancheysandor.applications.dominion.server.web.entity.UserRole;
import hu.kalmancheysandor.applications.dominion.server.web.repository.UserRepository;
import hu.kalmancheysandor.applications.dominion.server.web.configuration.security.SecurityUser;
import hu.kalmancheysandor.applications.dominion.server.web.configuration.security.SecurityUserAuthority;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@NoArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        User user = userRepository.findByIdentifier(identifier);

        if (user == null) {
            throw new UsernameNotFoundException("Invalid Identifier and password");
        }


        Set<SecurityUserAuthority> authorities = new HashSet<>();
        for (UserRole role : user.getAuthorities()) {
            authorities.add(new SecurityUserAuthority(role.getAuthority()));
        }
        SecurityUser securityUser = new SecurityUser();
        securityUser.setUsername(user.getIdentifier());
        securityUser.setPassword(user.getPassword());
        securityUser.setAuthorities(authorities);

        return securityUser;
    }
}
