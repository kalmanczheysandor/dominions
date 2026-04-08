package hu.kalmancheysandor.applications.dominions.apis.server.user.common.security.authentication;


import hu.kalmancheysandor.applications.dominions.apis.server.user.common.exception.SessionExpiredException;
import hu.kalmancheysandor.applications.dominions.apis.server.user.common.exception.permission.AccessActionNotGrantedException;
import hu.kalmancheysandor.applications.dominions.apis.server.user.common.exception.permission.AddActionNotGrantedException;
import hu.kalmancheysandor.applications.dominions.apis.server.user.common.exception.permission.DeleteActionNotGrantedException;
import hu.kalmancheysandor.applications.dominions.apis.server.user.common.exception.permission.EditActionNotGrantedException;
import hu.kalmancheysandor.applications.dominions.apis.server.user.common.security.SecurityPermissionPathGenerator;
import hu.kalmancheysandor.applications.dominions.apis.server.user.common.security.UserSecurityDetails;
import hu.kalmancheysandor.applications.dominions.apis.server.user.common.security.authentication.exception.UserIsNotAuthenticatedException;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

@AllArgsConstructor
public abstract class TAuthenticationService {

    public boolean hasAccessPermission(String resourceName) {
        return hasPermission(resourceName, "VIEW");
    }

    public boolean hasAddPermission(String resourceName) {
        return hasPermission(resourceName, "ADD");
    }

    public boolean hasEditPermission(String resourceName) {
        return hasPermission(resourceName, "EDIT");
    }

    public boolean hasDeletePermission(String resourceName) {
        return hasPermission(resourceName, "DELETE");
    }


    public boolean hasPermission(String resourceName, String action) {
        return hasPermission(resourceName + ":" + action);
    }

    public boolean hasPermission(String requiredPermission) {
        System.out.println("hasPermission.begin: " + requiredPermission);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null ) {
            throw new RuntimeException("Authentication is null");
        }

        if (!authentication.isAuthenticated()) {
            throw new UserIsNotAuthenticatedException();
        }
        if (authentication instanceof AnonymousAuthenticationToken) {
            throw new SessionExpiredException();
        }


        System.out.println("________PRINCIPAL: " + authentication.getPrincipal());


        // Check if the user has the required permission
        System.out.println("hasPermission.For");
        for (GrantedAuthority authority : authentication.getAuthorities()) {


            System.out.println("?------>>>>" + authority.getAuthority() + "?=" + requiredPermission);
            if (authority.getAuthority().equals(requiredPermission)) {
                System.out.println("Yes");
                return true;
            }

        }

        System.out.println("hasPermission:No 2");
        return false;
    }

    // TODO: eltuntetni mindenhonnan ezt a metodust
    @Deprecated
    public void assertHasPermission(String requiredPermission) {
        if (!hasPermission(requiredPermission)) {
            throw new AccessDeniedException("You do not have the required " + requiredPermission + " permission.");
        }
    }


    public void assertHasAccessPermission(String resourceName) {
        if (!hasAccessPermission(resourceName)) {
            throw new AccessActionNotGrantedException(resourceName);
        }
    }

    public void assertHasAccessPermission(SecurityPermissionPathGenerator path) {
        assertHasAccessPermission(path.generate());
    }


    public void assertHasAddPermission(String resourceName) {
        if (!hasAddPermission(resourceName)) {
            throw new AddActionNotGrantedException(resourceName);
        }
    }

    public void assertHasAddPermission(SecurityPermissionPathGenerator path) {
        assertHasAddPermission(path.generate());
    }


    public void assertHasEditPermission(String resourceName) {
        if (!hasEditPermission(resourceName)) {
            throw new EditActionNotGrantedException(resourceName);
        }
    }

    public void assertHasEditPermission(SecurityPermissionPathGenerator path) {
        assertHasEditPermission(path.generate());
    }


    public void assertHasDeletePermission(String resourceName) {
        if (!hasDeletePermission(resourceName)) {
            throw new DeleteActionNotGrantedException(resourceName);
        }
    }

    public void assertHasDeletePermission(SecurityPermissionPathGenerator path) {
        assertHasDeletePermission(path.generate());
    }


    /// //////////////////////////////////////////////////////////////
    /// //////////////////////////////////////////////////////////////
    /// //////////////////////////////////////////////////////////////
    /// //////////////////////////////////////////////////////////////
    /// //////////////////////////////////////////////////////////////
    /// //////////////////////////////////////////////////////////////
    /// //////////////////////////////////////////////////////////////
    /// //////////////////////////////////////////////////////////////
    /// //////////////////////////////////////////////////////////////


    public boolean isCurrentUserAuthenticated() {
        // Get the current authentication object
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Check if authentication is not null and is authenticated
        return authentication != null && authentication.isAuthenticated()
                && !(authentication.getPrincipal() instanceof String);  // Principal is not 'anonymousUser'
    }

    public int getCurrentAuthenticatedUserId() {
        UserSecurityDetails userDetails = getCurrentAuthenticatedPrincipalDetails();
        return userDetails.getId();
    }

    public UserSecurityDetails getCurrentAuthenticatedPrincipalDetails() {

        // Check: Whether anybody is logged in
        if (!isCurrentUserAuthenticated()) {
            throw new UserIsNotAuthenticatedException();
        }

        // Get the authentication object from the SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserSecurityDetails userDetails = (UserSecurityDetails) authentication.getPrincipal();
        return userDetails;

    }

}
