package hu.kalmancheysandor.applications.dominions.service.authentication;


import hu.kalmancheysandor.applications.dominions.servers.admin.configuration.security.SecurityUserDetails;
import hu.kalmancheysandor.applications.dominions.service.authentication.exception.NoAuthenticatedUserException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthenticationService {

    public boolean isAnyUserAuthenticated() {
        // Get the current authentication object
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Check if authentication is not null and is authenticated
        return authentication != null && authentication.isAuthenticated()
            && !(authentication.getPrincipal() instanceof String);  // Principal is not 'anonymousUser'
    }

    public int getCurrentAuthenticatedUserId() {
        SecurityUserDetails userDetails = getCurrentAuthenticatedPrincipalDetails();
        return userDetails.getId();
    }

    public SecurityUserDetails getCurrentAuthenticatedPrincipalDetails() {

        // Check: Whether anybody is logged in
        if (!isAnyUserAuthenticated()) {
            throw new NoAuthenticatedUserException();
        }

        // Get the authentication object from the SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecurityUserDetails userDetails = (SecurityUserDetails) authentication.getPrincipal();
        return userDetails;

    }

}
