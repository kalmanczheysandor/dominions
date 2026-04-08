package hu.kalmancheysandor.applications.dominions.servers.site.service.account.exception.recovery;

public class SiteAccountRecoveryVerificationTokenNotFoundException extends RuntimeException {
    private String verificationToken;

    public SiteAccountRecoveryVerificationTokenNotFoundException(String verificationToken) {
        this.verificationToken = verificationToken;
    }

    public String getVerificationToken() {
        return verificationToken;
    }

    @Override
    public String toString() {
        return "SiteAccountSignUpVerificationTokenNotFoundException{" +
                "verificationToken='" + verificationToken + '\'' +
                '}';
    }
}
