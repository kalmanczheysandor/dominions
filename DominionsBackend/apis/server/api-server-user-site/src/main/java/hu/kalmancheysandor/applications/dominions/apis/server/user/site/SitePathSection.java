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

        validTargets.add("Account.Settings");
        validTargets.add("Game.Play");
        validTargets.add("Game.Create");
        validTargets.add("Game.Lobby");

        this.setValidTargets(validTargets);
    }


    public SitePathSection account() {
        return new SitePathSection(this, "Account");
    }

    public SitePathSection settings() {
        return new SitePathSection(this, "Settings");
    }

    public SitePathSection game() {
        return new SitePathSection(this, "Game");
    }

    public SitePathSection create() {
        return new SitePathSection(this, "Create");
    }

    public SitePathSection lobby() {
        return new SitePathSection(this, "Lobby");
    }

    public SitePathSection play() {
        return new SitePathSection(this, "Play");
    }
}
