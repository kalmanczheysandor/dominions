package hu.kalmancheysandor.applications.dominions.service.account.user.exception;

import hu.kalmancheysandor.applications.dominions.service.exception.CustomException;
import hu.kalmancheysandor.applications.dominions.service.exception.CustomException;
import hu.kalmancheysandor.applications.dominions.service.exception.CustomException;

public class UserSelfDeleteException extends CustomException {

    private int userId;

    public UserSelfDeleteException(int userId) {
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
