package hu.kalmancheysandor.applications.dominions.servers.admin.configuration.security.permission;

import hu.kalmancheysandor.applications.dominions.servers.admin.service.exception.permission.EditActionNotGrantedException;
import hu.kalmancheysandor.applications.dominions.servers.admin.service.exception.permission.AccessActionNotGrantedException;
import hu.kalmancheysandor.applications.dominions.servers.admin.service.exception.permission.AddActionNotGrantedException;
import hu.kalmancheysandor.applications.dominions.servers.admin.service.exception.permission.DeleteActionNotGrantedException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityAuthorisation {

    private static SecurityAuthorisation securityAuthorisation = new SecurityAuthorisation();


    public boolean hasAccessPermission( String resourceName) {
        return hasPermission(resourceName,"VIEW");
    }

    public boolean hasAddPermission( String resourceName) {
        return hasPermission(resourceName,"ADD");
    }

    public boolean hasEditPermission( String resourceName) {
        return hasPermission(resourceName,"EDIT");
    }

    public boolean hasDeletePermission( String resourceName) {
        return hasPermission(resourceName,"DELETE");
    }



    public boolean hasPermission(String resourceName, String action) {
        return hasPermission(resourceName+":"+action);
    }

    public boolean hasPermission(String requiredPermission) {
        System.out.println("hasPermission.begin: "+requiredPermission);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            System.out.println("hasPermission:No 1");
            return false;
        }


        System.out.println("________PRINCIPAL: "+authentication.getPrincipal());


        // Check if the user has the required permission
        System.out.println("hasPermission.For");
        for (GrantedAuthority authority : authentication.getAuthorities()) {


            System.out.println("?------>>>>"+authority.getAuthority()+"?="+requiredPermission);
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
    public  static void assertHasPermission(String requiredPermission) {
        if (!securityAuthorisation.hasPermission(requiredPermission)) {
            throw new AccessDeniedException("You do not have the required "+requiredPermission+" permission.");
        }
    }


    public static void assertHasAccessPermission(String resourceName) {
        if (!securityAuthorisation.hasAccessPermission(resourceName)) {
            throw new AccessActionNotGrantedException(resourceName);
        }
    }
    public static void assertHasAccessPermission(SecurityPermissionPathGenerator path) {
        assertHasAccessPermission(path.generate());
    }


    public static void assertHasAddPermission(String resourceName) {
        if (!securityAuthorisation.hasAddPermission(resourceName)) {
            throw new AddActionNotGrantedException(resourceName);
        }
    }
    public static void assertHasAddPermission(SecurityPermissionPathGenerator path) {
        assertHasAddPermission(path.generate());
    }


    public static void assertHasEditPermission(String resourceName) {
        if (!securityAuthorisation.hasEditPermission(resourceName)) {
            throw new EditActionNotGrantedException(resourceName);
        }
    }
    public static void assertHasEditPermission(SecurityPermissionPathGenerator path) {
        assertHasEditPermission(path.generate());
    }


    public static void assertHasDeletePermission(String resourceName) {
        if (!securityAuthorisation.hasDeletePermission(resourceName)) {
            throw new DeleteActionNotGrantedException(resourceName);
        }
    }
    public static void assertHasDeletePermission(SecurityPermissionPathGenerator path) {
        assertHasDeletePermission(path.generate());
    }





}