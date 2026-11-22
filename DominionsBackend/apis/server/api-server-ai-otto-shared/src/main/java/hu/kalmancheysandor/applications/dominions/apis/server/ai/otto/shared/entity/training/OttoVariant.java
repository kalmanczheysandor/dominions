package hu.kalmancheysandor.applications.dominions.apis.server.ai.otto.shared.entity.training;


import hu.kalmancheysandor.applications.dominions.apis.util.uuid.UUIDIdentifiable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;


@Entity
@Table(name = "t_ai_otto_variant")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OttoVariant implements UUIDIdentifiable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "uuid", unique = true, nullable = false, updatable = false)
    private String uuid;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "evaluator_code", nullable = false)
    private String evaluatorCode;

    @Column(name = "is_enabled", nullable = false, columnDefinition = "TINYINT(1)")
    private boolean enabled;

    @Column(name = "date_created", nullable = false)
    private LocalDateTime dateCreated;

    @Column(name = "date_modified", nullable = true)
    private LocalDateTime dateModified;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        OttoVariant that = (OttoVariant) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "OttoVariant{" +
                "id=" + id +
                ", uuid='" + uuid + '\'' +
                ", name='" + name + '\'' +
                ", evaluatorCode='" + evaluatorCode + '\'' +
                ", enabled=" + enabled +
                ", dateCreated=" + dateCreated +
                ", dateModified=" + dateModified +
                '}';
    }
}