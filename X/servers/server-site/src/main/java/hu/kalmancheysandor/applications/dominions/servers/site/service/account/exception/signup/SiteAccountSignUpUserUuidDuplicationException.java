package hu.kalmancheysandor.applications.dominions.servers.site.service.account.exception.signup;

public class SiteAccountSignUpUserUuidDuplicationException extends RuntimeException {
private String userUuid;

    public SiteAccountSignUpUserUuidDuplicationException(String userUuid) {
        this.userUuid = userUuid;
    }

    public String getUserUuid() {
        return userUuid;
    }

    @Override
    public String toString() {
        return "SiteAccountSignUpUserUuidDuplicationException{" +
                "userUuid='" + userUuid + '\'' +
                '}';
    }
}
