package hu.kalmanczheysandor.applications.dominion.service;

import hu.kalmanczheysandor.applications.dominion.entity.User;
import hu.kalmanczheysandor.applications.dominion.entity.UserRole;
import hu.kalmanczheysandor.applications.dominion.repository.UserRepository;
import hu.kalmanczheysandor.applications.dominion.configuration.security.SecurityUser;
import hu.kalmanczheysandor.applications.dominion.configuration.security.SecurityUserAuthority;
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
