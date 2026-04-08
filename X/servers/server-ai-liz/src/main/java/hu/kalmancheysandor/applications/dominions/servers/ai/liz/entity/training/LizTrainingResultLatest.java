package hu.kalmancheysandor.applications.dominions.servers.ai.liz.entity.training;


import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;


@Entity
@IdClass(LizTrainingResultLatest.PrimaryKey.class)
@Table(name = "v_ai_liz_training_result_latest")
@Data
@Setter(AccessLevel.NONE) // Prevent Lombok from generating setters
public class LizTrainingResultLatest {

    @Id
    @Column(name="training_id", nullable=false)
    private Integer trainingId;

    @Id
    @Column(name="scenario_id", nullable=false)
    private Integer scenarioId;

    @Id
    @Column(name="player_id", nullable=false)
    private Integer playerId;

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

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            PrimaryKey that = (PrimaryKey) o;
            return Objects.equals(trainingId, that.trainingId) && Objects.equals(scenarioId, that.scenarioId) && Objects.equals(playerId, that.playerId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(trainingId, scenarioId, playerId);
        }
    }
}