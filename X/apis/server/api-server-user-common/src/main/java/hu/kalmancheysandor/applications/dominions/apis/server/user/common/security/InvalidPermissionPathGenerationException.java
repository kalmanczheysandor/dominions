package hu.kalmancheysandor.applications.dominions.apis.server.user.common.security;

public class InvalidPermissionPathGenerationException extends RuntimeException {
    private String permissionPath;
    public InvalidPermissionPathGenerationException(final String permissionPath) {
        this.permissionPath = permissionPath;
    }
}
