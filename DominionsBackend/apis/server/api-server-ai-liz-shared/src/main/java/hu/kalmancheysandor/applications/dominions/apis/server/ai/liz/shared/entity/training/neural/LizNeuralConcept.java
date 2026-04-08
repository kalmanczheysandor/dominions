package hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.entity.training.neural;


import hu.kalmancheysandor.applications.dominions.apis.util.uuid.UUIDIdentifiable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;


@Entity
@Table(name = "t_ai_liz_nn_concept")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LizNeuralConcept implements UUIDIdentifiable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "uuid", unique = true, nullable = false, updatable = false)
    private String uuid;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "conf_max_iterations_per_turn", nullable = false)
    private Integer confMaxIterationsPerTurn;
    @Column(name = "conf_max_turn", nullable = false)
    private Integer confMaxTurn;
    @Column(name = "conf_learning_rate", nullable = false)
    private BigDecimal confLearningRate;

    @Column(name = "is_enabled", nullable = false, columnDefinition = "TINYINT(1)")
    private boolean enabled;

    @Column(name = "date_created", nullable = false)
    private LocalDateTime dateCreated;

    @Column(name = "date_modified", nullable = true)
    private LocalDateTime dateModified;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        LizNeuralConcept that = (LizNeuralConcept) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "LizNeuralConcept{" +
                "id=" + id +
                ", uuid='" + uuid + '\'' +
                ", name='" + name + '\'' +
                ", confMaxIterationsPerTurn=" + confMaxIterationsPerTurn +
                ", confMaxTurn=" + confMaxTurn +
                ", confLearningRate=" + confLearningRate +
                ", enabled=" + enabled +
                ", dateCreated=" + dateCreated +
                '}';
    }
}