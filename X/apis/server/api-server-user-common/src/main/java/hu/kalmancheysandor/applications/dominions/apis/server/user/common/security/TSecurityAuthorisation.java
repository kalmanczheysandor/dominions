package hu.kalmancheysandor.applications.dominions.apis.server.user.common.security;


import hu.kalmancheysandor.applications.dominions.apis.server.user.common.exception.permission.AccessActionNotGrantedException;
import hu.kalmancheysandor.applications.dominions.apis.server.user.common.exception.permission.AddActionNotGrantedException;
import hu.kalmancheysandor.applications.dominions.apis.server.user.common.exception.permission.DeleteActionNotGrantedException;
import hu.kalmancheysandor.applications.dominions.apis.server.user.common.exception.permission.EditActionNotGrantedException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.HashMap;
import java.util.Map;

public abstract class TSecurityAuthorisation {

    private static TSecurityAuthorisation securityAuthorisation;
    private static final Map<String, TSecurityAuthorisation> instances = new HashMap<>();






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

        if (authentication == null || !authentication.isAuthenticated()) {
            System.out.println("hasPermission:No 1");
            return false;
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
        if (!securityAuthorisation.hasPermission(requiredPermission)) {
            throw new AccessDeniedException("You do not have the required " + requiredPermission + " permission.");
        }
    }


    public void assertHasAccessPermission(String resourceName) {
        if (!securityAuthorisation.hasAccessPermission(resourceName)) {
            throw new AccessActionNotGrantedException(resourceName);
        }
    }

    public  void assertHasAccessPermission(SecurityPermissionPathGenerator path) {
        assertHasAccessPermission(path.generate());
    }


    public  void assertHasAddPermission(String resourceName) {
        if (!securityAuthorisation.hasAddPermission(resourceName)) {
            throw new AddActionNotGrantedException(resourceName);
        }
    }

    public  void assertHasAddPermission(SecurityPermissionPathGenerator path) {
        assertHasAddPermission(path.generate());
    }


    public  void assertHasEditPermission(String resourceName) {
        if (!securityAuthorisation.hasEditPermission(resourceName)) {
            throw new EditActionNotGrantedException(resourceName);
        }
    }

    public  void assertHasEditPermission(SecurityPermissionPathGenerator path) {
        assertHasEditPermission(path.generate());
    }


    public  void assertHasDeletePermission(String resourceName) {
        if (!securityAuthorisation.hasDeletePermission(resourceName)) {
            throw new DeleteActionNotGrantedException(resourceName);
        }
    }

    public  void assertHasDeletePermission(SecurityPermissionPathGenerator path) {
        assertHasDeletePermission(path.generate());
    }


}