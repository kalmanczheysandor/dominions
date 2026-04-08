package hu.kalmancheysandor.applications.dominions.apis.server.user.common.exception.permission;

public class ActionNotGrantedException  extends RuntimeException {

    private String resource;
    private String action;

    public ActionNotGrantedException(String resource,String action) {
        super("The action '" + action + "' is not granted on resource " + resource);
        this.action = action;
        this.resource = resource;
    }

    public String getResource() {
        return resource;
    }

    public String getAction() {
        return action;
    }
}
