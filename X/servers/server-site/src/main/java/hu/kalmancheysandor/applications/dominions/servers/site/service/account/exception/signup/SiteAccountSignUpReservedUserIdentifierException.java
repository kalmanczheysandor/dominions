package hu.kalmancheysandor.applications.dominions.servers.site.service.account.exception.signup;

public class SiteAccountSignUpReservedUserIdentifierException extends RuntimeException {
private String identifier;

    public SiteAccountSignUpReservedUserIdentifierException(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

    @Override
    public String toString() {
        return "SiteAccountSignUpReservedUserIdentifierException{" +
                "identifier='" + identifier + '\'' +
                '}';
    }
}
