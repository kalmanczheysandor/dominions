package hu.kalmancheysandor.applications.dominions.servers.admin.entity.dog;


import hu.kalmancheysandor.applications.dominions.servers.admin.entity.breed.Breed;
import hu.kalmancheysandor.applications.dominions.servers.admin.entity.site.Site;
import hu.kalmancheysandor.applications.dominions.servers.admin.utils.uuid.UUIDIdentifiable;
import jakarta.persistence.*;
import lombok.Data;
import java.util.Objects;

@Entity
@Table(name = "t_dog")
@Data
public class Dog implements UUIDIdentifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="uuid",unique = true, nullable = false, updatable = false)
    private String uuid;

    @Column(name="prn", unique = true, nullable=false)
    private String prn;

    @Column(name="name", nullable=false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "breed_id")
    private Breed breed;

    @ManyToOne
    @JoinColumn(name = "site_id")
    private Site site;

    @Column(name="note", nullable=false,columnDefinition = "LONGTEXT")
    private String note;

    @Column(name="is_enabled",nullable = false,columnDefinition = "TINYINT(1)")
    private boolean enabled;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dog user = (Dog) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "dog{" +
            "id=" + id +
            ", prn='" + prn + '\'' +
            ", name='" + name + '\'' +
            '}';
    }

}
