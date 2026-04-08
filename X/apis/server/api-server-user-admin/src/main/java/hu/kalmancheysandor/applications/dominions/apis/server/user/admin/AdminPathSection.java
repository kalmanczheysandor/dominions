package hu.kalmancheysandor.applications.dominions.apis.server.user.admin;


import hu.kalmancheysandor.applications.dominions.apis.server.user.common.security.SecurityPermissionPathGenerator;

import java.util.HashSet;
import java.util.Set;

public class AdminPathSection extends SecurityPermissionPathGenerator {

    public AdminPathSection() {
        collectingValidTargets();
    }

    public AdminPathSection(SecurityPermissionPathGenerator parentSection, String targetName) {
        super(parentSection, targetName);
        collectingValidTargets();
    }

    private void collectingValidTargets() {
        Set<String> validTargets = new HashSet<String>();

        validTargets.add("Game.Scenario");

        validTargets.add("Account.Admin.User");
        validTargets.add("Account.Admin.PermissionGroup");
        validTargets.add("Account.Admin.Settings");

        validTargets.add("Account.Site.User");
        validTargets.add("Account.Site.PermissionGroup");


        this.setValidTargets(validTargets);
    }


    public AdminPathSection game() {
        return new AdminPathSection(this, "Game");
    }
    public AdminPathSection scenario() {
        return new AdminPathSection(this, "Scenario");
    }



    public AdminPathSection account() {
        return new AdminPathSection(this, "Account");
    }

    public AdminPathSection site() {
        return new AdminPathSection(this, "Site");
    }

    public AdminPathSection admin() {
        return new AdminPathSection(this, "Admin");
    }

    public AdminPathSection user() {
        return new AdminPathSection(this, "User");
    }

    public AdminPathSection settings() {
        return new AdminPathSection(this, "Settings");
    }

    public AdminPathSection permissionGroup() {
        return new AdminPathSection(this, "PermissionGroup");
    }

}
