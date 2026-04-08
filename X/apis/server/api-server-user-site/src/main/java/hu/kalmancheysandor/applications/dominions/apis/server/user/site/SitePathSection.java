package hu.kalmancheysandor.applications.dominions.apis.server.user.site;


import hu.kalmancheysandor.applications.dominions.apis.server.user.common.security.SecurityPermissionPathGenerator;

import java.util.HashSet;
import java.util.Set;

public class SitePathSection extends SecurityPermissionPathGenerator {

    public SitePathSection() {
        collectingValidTargets();
    }

    public SitePathSection(SecurityPermissionPathGenerator parentSection, String targetName) {
        super(parentSection, targetName);
        collectingValidTargets();
    }

    private void collectingValidTargets() {
        Set<String> validTargets = new HashSet<String>();

        validTargets.add("Account.SignUp");
        validTargets.add("Account.SignIn");
        validTargets.add("Account.Settings");

        this.setValidTargets(validTargets);
    }


    public SitePathSection account() {
        return new SitePathSection(this, "Account");
    }

    public SitePathSection signUp() {
        return new SitePathSection(this, "SignUp");
    }
    public SitePathSection signIn() {
        return new SitePathSection(this, "SignIn");
    }

    public SitePathSection settings() {
        return new SitePathSection(this, "Settings");
    }


}
