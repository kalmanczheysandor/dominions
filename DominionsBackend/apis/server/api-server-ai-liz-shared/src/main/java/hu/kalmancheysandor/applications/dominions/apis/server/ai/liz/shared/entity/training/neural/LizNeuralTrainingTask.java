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
@IdClass(LizNeuralTrainingTask.PrimaryKey.class)
@Table(name = "t_ai_liz_nn_training_task")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LizNeuralTrainingTask {
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

    @Enumerated(EnumType.STRING)
    @Column(name = "task_phase", nullable = false)
    private TaskPhase taskPhase;

    @Enumerated(EnumType.STRING)
    @Column(name = "task_result",  nullable = false)
    private TaskResult taskResult;


    @Column(name = "date_created", nullable = false)
    private LocalDateTime dateCreated;

    @Column(name = "date_modified", nullable = true)
    private LocalDateTime dateModified;

    @Column(name = "date_finished", nullable = true)
    private LocalDateTime dateFinished;


    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
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

    public enum TaskPhase {
        CREATED,
        QUEUED,
        PAUSED,
        FINISHED
    }

    public enum TaskResult {
        PENDING,
        FAILED,
        SUCCEEDED,
        CANCELED
    }
}