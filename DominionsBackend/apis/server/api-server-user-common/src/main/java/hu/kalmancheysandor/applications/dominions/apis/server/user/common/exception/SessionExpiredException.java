package hu.kalmancheysandor.applications.dominions.apis.server.user.common.exception;

import hu.kalmancheysandor.applications.dominions.apis.server.user.common.exception.permission.ActionNotGrantedException;

public class SessionExpiredException extends RuntimeException {
    public SessionExpiredException() {
    }
}
