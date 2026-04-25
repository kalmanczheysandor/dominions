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
        validTargets.add("Ai.Liz.Character");
        validTargets.add("Ai.Liz.Variant");
        validTargets.add("Ai.Liz.Concept");

        validTargets.add("Ai.Hugo.Character");
        validTargets.add("Ai.Hugo.Variant");


        validTargets.add("Account.Admin.User");
        validTargets.add("Account.Admin.PermissionGroup");
        validTargets.add("Account.Admin.Settings");

        validTargets.add("Account.Site.User");
        validTargets.add("Account.Site.PermissionGroup");


        this.setValidTargets(validTargets);
    }


    public AdminPathSection ai() {
        return new AdminPathSection(this, "Ai");
    }
    public AdminPathSection liz() {
        return new AdminPathSection(this, "Liz");
    }
    public AdminPathSection hugo() {
        return new AdminPathSection(this, "Hugo");
    }
    public AdminPathSection character() {return new AdminPathSection(this, "Character");}
    public AdminPathSection variant() {
        return new AdminPathSection(this, "Variant");
    }
    public AdminPathSection concept() {
        return new AdminPathSection(this, "Concept");
    }

    public AdminPathSection training() {
        return new AdminPathSection(this, "Training");
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
