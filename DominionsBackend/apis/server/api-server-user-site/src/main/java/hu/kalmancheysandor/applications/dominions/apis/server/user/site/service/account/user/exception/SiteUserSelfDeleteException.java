package hu.kalmancheysandor.applications.dominions.apis.server.user.site.service.account.user.exception;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.CustomException;

public class SiteUserSelfDeleteException extends CustomException {

    private int userId;

    public SiteUserSelfDeleteException(int userId) {
        super("UserSelfDeleteException");
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "UserSelfDeleteException{" +
            "userId=" + userId +
            '}';
    }
}
