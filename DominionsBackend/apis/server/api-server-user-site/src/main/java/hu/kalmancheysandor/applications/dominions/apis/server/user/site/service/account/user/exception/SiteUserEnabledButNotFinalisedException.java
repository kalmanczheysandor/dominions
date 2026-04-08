package hu.kalmancheysandor.applications.dominions.apis.server.user.site.service.account.user.exception;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.CustomException;

public class SiteUserEnabledButNotFinalisedException extends CustomException {
    public SiteUserEnabledButNotFinalisedException() {
        super("UserEnabledButNotFinalisedException");
    }
}
