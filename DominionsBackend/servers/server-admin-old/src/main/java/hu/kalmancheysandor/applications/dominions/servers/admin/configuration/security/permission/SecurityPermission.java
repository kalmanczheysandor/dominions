package hu.kalmancheysandor.applications.dominions.servers.admin.configuration.security.permission;

import org.springframework.security.core.GrantedAuthority;

import java.util.HashSet;
import java.util.Set;

public class SecurityPermission implements GrantedAuthority {

    private final String permission;

    public SecurityPermission(String permission) {
        this.permission = permission;
    }

    @Override
    public String getAuthority() {
        return permission;
    }


    public static PathSection generator() {
        return new PathSection();
    }


    @Override
    public String toString() {
        return "SecurityPermission{" +
            "permission='" + permission + '\'' +
            '}';
    }

    public static class PathSection extends SecurityPermissionPathGenerator {

        public PathSection() {
            collectingValidTargets();
        }

        public PathSection(SecurityPermissionPathGenerator parentSection, String targetName) {
            super(parentSection, targetName);
            collectingValidTargets();
        }

        private void collectingValidTargets() {
            Set<String> validTargets = new HashSet<String>();
            validTargets.add("Dashboard");
            validTargets.add("Site");
            validTargets.add("Dog");
            validTargets.add("Breed");

            validTargets.add("Account.User");
            validTargets.add("Account.PermissionGroup");
            validTargets.add("Account.Profile");

            this.setValidTargets(validTargets);
        }



        public PathSection account() {
            return new PathSection(this,"Account");
        }
        public PathSection site() {
            return new PathSection(this,"Site");
        }
        public PathSection dog() {
            return new PathSection(this,"Dog");
        }
        public PathSection breed() {
            return new PathSection(this,"Breed");
        }
        public PathSection user() {
            return new PathSection(this,"User");
        }

        public PathSection profile() {
            return new PathSection(this,"Profile");
        }
        public PathSection permissionGroup() {
            return new PathSection(this,"PermissionGroup");
        }

    }


}
