package hu.kalmancheysandor.applications.dominions.servers.site.entity.account;


import hu.kalmancheysandor.applications.dominions.apis.util.uuid.UUIDIdentifiable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "t_site_account_signup_verification")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class SiteAccountSignUpVerification implements UUIDIdentifiable {

    @Id
    @Column(name = "uuid", unique = true, nullable = false, updatable = false)
    private String uuid;

    @Column(name = "user_uuid", nullable = false,unique = true)
    private String userUuid;

    @Column(name = "date_created", nullable = false)
    private LocalDateTime dateCreated;

    @Column(name = "date_expiration", nullable = false)
    private LocalDateTime dateExpiration;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SiteAccountSignUpVerification that = (SiteAccountSignUpVerification) o;
        return Objects.equals(uuid, that.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(uuid);
    }
}
