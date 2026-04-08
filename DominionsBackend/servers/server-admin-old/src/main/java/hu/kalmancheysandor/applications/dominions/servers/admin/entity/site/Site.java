package hu.kalmancheysandor.applications.dominions.servers.admin.entity.site;


import hu.kalmancheysandor.applications.dominions.servers.admin.utils.uuid.UUIDIdentifiable;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Objects;

@Entity
@Table(name = "t_site")
@Data
public class Site implements UUIDIdentifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="uuid",unique = true, nullable = false, updatable = false)
    private String uuid;

    @Column(name="name", nullable=false)
    private String name;

    @Column(name="address", nullable=false)
    private String address;


    @Column(name="note", nullable=false,columnDefinition = "LONGTEXT")
    private String note;

    @Column(name="is_enabled",nullable = false,columnDefinition = "TINYINT(1)")
    private boolean enabled;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Site user = (Site) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Site{" +
            "id=" + getId() +
            ", uuid='" + getUuid() + '\'' +
            ", name='" + getName() + '\'' +
            ", address='" + getAddress() + '\'' +
            ", enabled=" + isEnabled() +
            '}';
    }
}
