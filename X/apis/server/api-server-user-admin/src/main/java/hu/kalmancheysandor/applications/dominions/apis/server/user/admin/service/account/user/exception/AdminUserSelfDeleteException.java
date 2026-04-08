package hu.kalmancheysandor.applications.dominions.apis.server.user.admin.service.account.user.exception;


import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.CustomException;

public class AdminUserSelfDeleteException extends CustomException {

    private int userId;

    public AdminUserSelfDeleteException(int userId) {
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
