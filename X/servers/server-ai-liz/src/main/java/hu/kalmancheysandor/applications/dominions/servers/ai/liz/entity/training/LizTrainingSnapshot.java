package hu.kalmancheysandor.applications.dominions.servers.ai.liz.entity.training;


import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;


@Entity
@IdClass(LizTrainingSnapshot.PrimaryKey.class)
@Table(name = "t_ai_liz_training_snapshot")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LizTrainingSnapshot {

    @Id
    @Column(name="training_id", nullable=false)
    private Integer trainingId;

    @Id
    @Column(name="scenario_id", nullable=false)
    private Integer scenarioId;

    @Id
    @Column(name="player_id", nullable=false)
    private Integer playerId;

    @Id
    @Column(name="turn", nullable=false)
    private Integer turn;

    @Column(name="iteration_count", nullable=false)
    private Integer iterationCount;

    @Column(name="precision_rate",precision = 5, scale = 2, nullable = false)
    @DecimalMin(value = "0.00", inclusive = true)
    @DecimalMax(value = "100.00", inclusive = true)
    private BigDecimal precisionRate;

    @Column(name = "date_created", nullable = false)
    private LocalDateTime dateCreated;



    public static class PrimaryKey implements Serializable {
        private Integer trainingId;
        private Integer scenarioId;
        private Integer playerId;
        private Integer turn;

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            PrimaryKey that = (PrimaryKey) o;
            return Objects.equals(trainingId, that.trainingId) && Objects.equals(scenarioId, that.scenarioId) && Objects.equals(playerId, that.playerId) && Objects.equals(turn, that.turn);
        }

        @Override
        public int hashCode() {
            return Objects.hash(trainingId, scenarioId, playerId, turn);
        }
    }
}