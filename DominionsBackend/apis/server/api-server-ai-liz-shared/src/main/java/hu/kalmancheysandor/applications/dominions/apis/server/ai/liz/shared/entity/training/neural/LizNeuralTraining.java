package hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.entity.training.neural;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;


@Entity
@IdClass(LizNeuralTraining.PrimaryKey.class)
@Table(name = "t_ai_liz_nn_training")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LizNeuralTraining  {
    @Id
    @Column(name = "concept_id", nullable = false)
    private Integer conceptId;
    @Id
    @Column(name = "execution_id", nullable = false)
    private Integer executionId;
    @Id
    @Column(name = "scenario_id", nullable = false)
    private Integer scenarioId;

    @Column(name = "date_created", nullable = false)
    private LocalDateTime dateCreated;



    public static class PrimaryKey implements Serializable {
        private Integer conceptId;
        private Integer executionId;
        private Integer scenarioId;

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            PrimaryKey that = (PrimaryKey) o;
            return Objects.equals(conceptId, that.conceptId) && Objects.equals(executionId, that.executionId) && Objects.equals(scenarioId, that.scenarioId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(conceptId, executionId, scenarioId);
        }
    }

}