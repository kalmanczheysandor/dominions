package hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.entity.training.neural;


import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;


@Entity
@IdClass(LizNeuralTrainingResult.PrimaryKey.class)
@Table(name = "t_ai_liz_nn_training_result")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LizNeuralTrainingResult {
    @Id
    @Column(name = "concept_id", nullable = false)
    private Integer conceptId;
    @Id
    @Column(name = "execution_id", nullable = false)
    private Integer executionId;
    @Id
    @Column(name = "scenario_id", nullable = false)
    private Integer scenarioId;
    @Id
    @Column(name = "player_id", nullable = false)
    private Integer playerId;

    @Column(name="precision_rate",precision = 5, scale = 2, nullable = false)
    @DecimalMin(value = "0.00", inclusive = true)
    @DecimalMax(value = "100.00", inclusive = true)
    private BigDecimal precisionRate;


    @Column(name="turn_count",nullable = false)
    @Min(0)
    private int turnCount;


    @Column(name="turn_best",nullable = false)
    @Min(0)
    private int turnBest;


    @Column(name = "date_created", nullable = false)
    private LocalDateTime dateCreated;

    @Column(name = "date_modified", nullable = true)
    private LocalDateTime dateModified;



    public static class PrimaryKey implements Serializable {
        private Integer conceptId;
        private Integer executionId;
        private Integer scenarioId;
        private Integer playerId;

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            PrimaryKey that = (PrimaryKey) o;
            return Objects.equals(conceptId, that.conceptId) && Objects.equals(executionId, that.executionId) && Objects.equals(scenarioId, that.scenarioId) && Objects.equals(playerId, that.playerId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(conceptId, executionId, scenarioId, playerId);
        }
    }
}