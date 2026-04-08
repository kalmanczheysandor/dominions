package hu.kalmancheysandor.applications.dominions.servers.site.service.account.exception.recovery;

public class SiteAccountRecoveryUserUuidDuplicationException extends RuntimeException {
private String userUuid;

    public SiteAccountRecoveryUserUuidDuplicationException(String userUuid) {
        this.userUuid = userUuid;
    }

    public String getUserUuid() {
        return userUuid;
    }

    @Override
    public String toString() {
        return "SiteAccountRecoveryUserUuidDuplicationException{" +
                "userUuid='" + userUuid + '\'' +
                '}';
    }
}
