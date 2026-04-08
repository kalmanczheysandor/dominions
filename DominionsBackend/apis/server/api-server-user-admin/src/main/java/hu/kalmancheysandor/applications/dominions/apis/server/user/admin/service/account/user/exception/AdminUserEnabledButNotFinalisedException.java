package hu.kalmancheysandor.applications.dominions.apis.server.user.admin.service.account.user.exception;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.CustomException;

public class AdminUserEnabledButNotFinalisedException extends CustomException {
    public AdminUserEnabledButNotFinalisedException() {
        super("UserEnabledButNotFinalisedException");
    }
}
