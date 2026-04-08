package hu.kalmancheysandor.applications.dominions.servers.site.service.account.exception.signup;

public class SiteAccountSignUpVerificationTokenNotFoundException extends RuntimeException {
    private String verificationToken;

    public SiteAccountSignUpVerificationTokenNotFoundException(String verificationToken) {
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
