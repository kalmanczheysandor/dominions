package hu.kalmancheysandor.applications.dominions.service.authentication;


import hu.kalmancheysandor.applications.dominions.servers.admin.configuration.security.SecurityUserDetails;
import hu.kalmancheysandor.applications.dominions.service.CustomUserDetailsService;
import hu.kalmancheysandor.applications.dominions.service.authentication.exception.NoAuthenticatedUserException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private  AuthenticationManager authenticationManager;

    private  CustomUserDetailsService customUserDetailsService;

    public AuthService(AuthenticationManager authenticationManager, CustomUserDetailsService customUserDetailsService) {
        this.authenticationManager = authenticationManager;
        this.customUserDetailsService = customUserDetailsService;
    }

    public Authentication login(String username, String password) {
        if (!customUserDetailsService.isEnabled(username)) {
            throw new UsernameNotFoundException("User is not enabled.");
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        Authentication auth = authenticationManager.authenticate(authenticationToken);

        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        System.out.println("Principal-Username:" +userDetails.getUsername());

        return auth;
    }


    public boolean isAnyUserAuthenticated() {
        // Get the current authentication object
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Check if authentication is not null and is authenticated
        return authentication != null && authentication.isAuthenticated()
            && !(authentication.getPrincipal() instanceof String);  // Principal is not 'anonymousUser'
    }


    public Integer getCurrentAuthenticatedUserId() {

        if (!isAnyUserAuthenticated()) {
            throw new NoAuthenticatedUserException();
        }

        // Get the authentication object from the SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Check if the authentication is present and the principal is an instance of the custom UserDetails
        if (authentication != null && authentication.getPrincipal() instanceof SecurityUserDetails) {
            SecurityUserDetails userDetails = (SecurityUserDetails) authentication.getPrincipal();
            return userDetails.getId();  // Retrieve the user ID from CustomUserDetails
        }

        // Return null or throw an exception if no user is authenticated
        return null;
    }


    public SecurityUserDetails getCurrentAuthenticatedUserDetails() {

        if (!isAnyUserAuthenticated()) {
            throw new NoAuthenticatedUserException();
        }

        // Get the authentication object from the SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Check if the authentication is present and the principal is an instance of the custom UserDetails
        if (authentication != null && authentication.getPrincipal() instanceof SecurityUserDetails) {
            SecurityUserDetails userDetails = (SecurityUserDetails) authentication.getPrincipal();
            return userDetails;
        }

        // Return null or throw an exception if no user is authenticated
        throw new RuntimeException();
    }
}
