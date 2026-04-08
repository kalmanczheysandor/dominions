package hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.entity.training.neural;


import hu.kalmancheysandor.applications.dominions.apis.util.uuid.UUIDIdentifiable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;


@Entity
@Table(name = "t_ai_liz_nn_execution")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Access(AccessType.FIELD)
@Builder
public class LizNeuralExecution implements UUIDIdentifiable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "uuid", unique = true, nullable = false, updatable = false)
    private String uuid;

    @Column(name = "name", unique = true, nullable = false, updatable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "concept_id", nullable = false)
    private LizNeuralConcept concept;

    @Enumerated(EnumType.STRING)
    @Column(name = "process_phase", nullable = false)
    private ProcessPhase processPhase;


    @Min(0)
    @Setter(lombok.AccessLevel.NONE)
    @Column(name = "task_all_count", nullable = false)
    private int taskAllCount;

    @Min(0)
    @Setter(lombok.AccessLevel.NONE)
    @Column(name = "task_phase_created_count", nullable = false)
    private int taskPhaseCreatedCount;

    @Min(0)
    @Setter(lombok.AccessLevel.NONE)
    @Column(name = "task_phase_queued_count", nullable = false)
    private int taskPhaseQueuedCount;

    @Min(0)
    @Setter(lombok.AccessLevel.NONE)
    @Column(name = "task_phase_paused_count", nullable = false)
    private int taskPhasePausedCount;

    @Min(0)
    @Setter(lombok.AccessLevel.NONE)
    @Column(name = "task_phase_finished_count", nullable = false)
    private int taskPhaseFinishedCount;

    @Min(0)
    @Setter(lombok.AccessLevel.NONE)
    @Column(name = "task_result_pending_count", nullable = false)
    private int taskResultPendingCount;

    @Min(0)
    @Setter(lombok.AccessLevel.NONE)
    @Column(name = "task_result_failed_count", nullable = false)
    private int taskResultFailedCount;

    @Min(0)
    @Setter(lombok.AccessLevel.NONE)
    @Column(name = "task_result_succeeded_count", nullable = false)
    private int taskResultSucceededCount;

    @Min(0)
    @Setter(lombok.AccessLevel.NONE)
    @Column(name = "task_result_canceled_count", nullable = false)
    private int taskResultCanceledCount;

//    @Setter(lombok.AccessLevel.NONE)
//    @Column(name = "is_applicable", nullable = false, columnDefinition = "TINYINT(1)")
//    private boolean applicable;

    @Setter(lombok.AccessLevel.NONE)
    @Column(name = "date_created", nullable = false)
    private LocalDateTime dateCreated;

    @Column(name = "date_modified", nullable = true)
    private LocalDateTime dateModified;

    @Column(name = "date_finished", nullable = true)
    private LocalDateTime dateFinished;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        LizNeuralExecution that = (LizNeuralExecution) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }


    public enum ProcessPhase {
        INITIALISED,
        RUNNING,
        PAUSED,
        FINISHED;
    }

}