package hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.entity.training;


import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.entity.training.neural.LizNeuralConcept;
import hu.kalmancheysandor.applications.dominions.apis.util.uuid.UUIDIdentifiable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;


@Entity
@Table(name = "t_ai_liz_variant")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LizVariant implements UUIDIdentifiable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "uuid", unique = true, nullable = false, updatable = false)
    private String uuid;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "conf_search_depth",  nullable = false)
    private Integer confSearchDepth;



    @ManyToOne
    @JoinColumn(name = "concept_id",nullable = false)
    private LizNeuralConcept concept;



    @Column(name = "is_enabled", nullable = false, columnDefinition = "TINYINT(1)")
    private boolean enabled;

    @Column(name = "date_created", nullable = false)
    private LocalDateTime dateCreated;

    @Column(name = "date_modified", nullable = true)
    private LocalDateTime dateModified;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        LizVariant that = (LizVariant) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "LizVariant{" +
                "id=" + id +
                ", uuid='" + uuid + '\'' +
                ", name='" + name + '\'' +
                ", confSearchDepth=" + confSearchDepth +
                ", enabled=" + enabled +
                ", dateCreated=" + dateCreated +
                '}';
    }
}