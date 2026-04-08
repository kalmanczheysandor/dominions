package hu.kalmancheysandor.applications.dominions.servers.site.service.account.exception.recovery;

public class SiteAccountRecoveryIdentifierNotFoundException extends RuntimeException {
private String identifier;

    public SiteAccountRecoveryIdentifierNotFoundException(String identifier) {
        this.identifier = identifier;
    }
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public String toString() {
        return "SiteAccountRecoveryIdentifierNotFoundException{" +
                "identifier='" + identifier + '\'' +
                '}';
    }
}
