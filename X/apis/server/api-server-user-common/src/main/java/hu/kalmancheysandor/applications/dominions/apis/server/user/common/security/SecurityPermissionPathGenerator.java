package hu.kalmancheysandor.applications.dominions.apis.server.user.common.security;


import java.util.HashSet;
import java.util.Set;

public class SecurityPermissionPathGenerator {
    private String nodeName = null;
    private SecurityPermissionPathGenerator parentNode = null;
    private String fullName = null;
    private boolean rootNode = true;

    private Set<String> validTargets = new HashSet<String>();

    public SecurityPermissionPathGenerator() {
    }

    public SecurityPermissionPathGenerator(SecurityPermissionPathGenerator parentNode, String nodeName) {
        rootNode = false;
        initialise(parentNode, nodeName);
    }

    private void initialise(SecurityPermissionPathGenerator parentNode, String nodeName) {
        this.parentNode = parentNode;
        this.nodeName = nodeName;

        if (parentNode == null) {
            this.fullName = nodeName;
        }
        else if (parentNode.isRootNode()) {
            this.fullName = nodeName;
        } else {
            this.fullName = parentNode.getFullName() + "." + nodeName;
        }
    }

    public String withAccessPermission() {
        return generate("VIEW");
    }

    public String withViewPermission() {
        return generate("VIEW");
    }

    public String withAddPermission() {
        return generate("ADD");
    }

    public String withEditPermission() {
        return generate("EDIT");
    }

    public String withDeletePermission() {
        return generate("DELETE");
    }

    private String generate(String permissionName) {
        if (!isValidTarget(getFullName())) {
            throw new InvalidPermissionPathGenerationException(getFullName());
        }

        String value = this.getFullName() + ":" + permissionName;
        return value;
    }

    public String generate() {
        if (!isValidTarget(getFullName())) {
            throw new InvalidPermissionPathGenerationException(getFullName());
        }

        return this.getFullName();
    }
    protected void setValidTargets(Set<String> validTargets) {
        this.validTargets = validTargets;
    }

    protected String getFullName() {
        return fullName;
    }

    protected boolean isValidTarget(String targetName) {
        return validTargets.contains(targetName);
    }

    private boolean isRootNode() {
        return rootNode;
    }
}
