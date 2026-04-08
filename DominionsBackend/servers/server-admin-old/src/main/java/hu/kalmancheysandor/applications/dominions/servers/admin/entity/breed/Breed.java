package hu.kalmancheysandor.applications.dominions.servers.admin.entity.breed;


import hu.kalmancheysandor.applications.dominions.servers.admin.utils.uuid.UUIDIdentifiable;
import jakarta.persistence.*;
import lombok.Data;
import java.util.Objects;

@Entity
@Table(name = "t_breed")
@Data
public class Breed implements UUIDIdentifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "uuid", unique = true, nullable = false, updatable = false)
    private String uuid;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "is_enabled", nullable = false, columnDefinition = "TINYINT(1)")
    private boolean enabled;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Breed user = (Breed) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "User{  id=" + id +", name='" + name +"}";
    }
}
