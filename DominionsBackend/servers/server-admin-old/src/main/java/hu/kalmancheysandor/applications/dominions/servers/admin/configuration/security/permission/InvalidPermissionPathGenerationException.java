package hu.kalmancheysandor.applications.dominions.servers.admin.configuration.security.permission;

public class InvalidPermissionPathGenerationException extends RuntimeException {
    private String permissionPath;
    public InvalidPermissionPathGenerationException(final String permissionPath) {
        this.permissionPath = permissionPath;
    }
}
