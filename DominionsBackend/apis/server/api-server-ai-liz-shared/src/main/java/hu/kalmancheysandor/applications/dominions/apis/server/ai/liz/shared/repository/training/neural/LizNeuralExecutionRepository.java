package hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.repository.training.neural;


import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.entity.history.LizHistoryScenario;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.entity.training.neural.LizNeuralExecution;

import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.entity.training.neural.LizNeuralTrainingTask;
import hu.kalmancheysandor.applications.dominions.apis.util.uuid.UUIDRepository;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface LizNeuralExecutionRepository extends JpaRepository<LizNeuralExecution, String>, UUIDRepository<LizNeuralExecution, String> {

    public LizNeuralExecution findById(int lizNeuralExecutionId);

    public LizNeuralExecution findByUuid(String lizNeuralExecutionUuid);

    public List<LizNeuralExecution> findAll();

    public void deleteById(int lizNeuralExecutionId);

    public void deleteByUuid(String lizNeuralExecutionUuid);

    public boolean existsById(int lizNeuralExecutionId);

    public boolean existsByUuid(String lizNeuralExecutionUuid);

    public LizNeuralExecution save(LizNeuralExecution lizNeuralExecution);

    @Query("SELECT t FROM  LizNeuralExecution t")
    List<LizNeuralExecution> listAll();

    @Lock(LockModeType.PESSIMISTIC_WRITE) // treated as: SELECT ....FOR UPDATE
    @Query("SELECT t FROM LizNeuralExecution t WHERE t.id = :executionId")
    @QueryHints({
            @QueryHint(name = "jakarta.persistence.lock.timeout", value = "10000") // 10000 milisecond = 10 sec
    })
    LizNeuralExecution accessByIdWithExclusiveLock(@Param("executionId") int executionId);


    @Query("""
            SELECT CASE WHEN EXISTS (
                        SELECT 1 FROM LizNeuralExecution t WHERE t.processPhase!='FINISHED' and t.concept.id=:conceptId
                                    )
                THEN TRUE ELSE FALSE END
            """)
    boolean isAnyUnfinished(@Param("conceptId") int conceptId);

    @Query("""
            SELECT CASE WHEN EXISTS (
                        SELECT 1 FROM LizNeuralExecution t WHERE t.processPhase='RUNNING' and t.concept.id=:conceptId
                                    )
                THEN TRUE ELSE FALSE END
            """)
    boolean isAnyRunning(@Param("conceptId") int conceptId);

    @Query("""
            SELECT CASE WHEN EXISTS (
                        SELECT 1 FROM LizNeuralExecution t WHERE t.processPhase='PAUSED' and t.concept.id=:conceptId
                                    )
                THEN TRUE ELSE FALSE END
            """)
    boolean isAnyPaused(@Param("conceptId") int conceptId);


    @Query("SELECT t FROM LizNeuralExecution t WHERE t.processPhase!='FINISHED' and t.concept.id=:conceptId")
    LizNeuralExecution findFirstUnfinished(@Param("conceptId") int conceptId);


    @Query("SELECT t FROM LizNeuralExecution t WHERE t.processPhase='PAUSED' and t.concept.id=:conceptId")
    LizNeuralExecution findFirstPaused(@Param("conceptId") int conceptId);


    @Query("SELECT t FROM LizNeuralExecution t WHERE t.processPhase='RUNNING' and t.concept.id=:conceptId")
    LizNeuralExecution findFirstRunning(@Param("conceptId") int conceptId);


    @Query("""
            SELECT t FROM LizNeuralExecution t WHERE t.id = (
                SELECT MAX(e.id)
                FROM LizNeuralExecution e
                WHERE e.processPhase='FINISHED' AND
                      e.concept.id=:conceptId
            )
            """)
    LizNeuralExecution findLatestFinished(@Param("conceptId") int conceptId);


    @Query("""
            SELECT t FROM LizNeuralExecution t WHERE t.id = (
                SELECT MAX(e.id)
                FROM LizNeuralExecution e
                WHERE e.concept.id=:conceptId
            )
            """)
    LizNeuralExecution findLatestExisting(@Param("conceptId") int conceptId);

    @Modifying
    @Query("""
               UPDATE LizNeuralExecution t1
                SET
                t1.taskAllCount=t1.taskAllCount+:deltaOfTaskAllCount,
                t1.taskPhaseCreatedCount=t1.taskPhaseCreatedCount+:deltaOfTaskPhaseCreatedCount,
                t1.taskPhaseQueuedCount=t1.taskPhaseQueuedCount+:deltaOfTaskPhaseQueuedCount,
                t1.taskPhasePausedCount=t1.taskPhasePausedCount+:deltaOfTaskPhasePausedCount,
                t1.taskPhaseFinishedCount=t1.taskPhaseFinishedCount+:deltaOfTaskPhaseFinishedCount
               WHERE t1.id = :executionId
            """)
    void adjustTaskPhaseCounters(
            @Param("executionId") int executionId,
            @Param("deltaOfTaskAllCount") int deltaOfTaskAllCount,
            @Param("deltaOfTaskPhaseCreatedCount") int deltaOfTaskPhaseCreatedCount,
            @Param("deltaOfTaskPhaseQueuedCount") int deltaOfTaskPhaseQueuedCount,
            @Param("deltaOfTaskPhasePausedCount") int deltaOfTaskPhasePausedCount,
            @Param("deltaOfTaskPhaseFinishedCount") int deltaOfTaskPhaseFinishedCount
    );


    @Modifying
    @Query("""
               UPDATE LizNeuralExecution t1
                SET
                t1.taskResultPendingCount=t1.taskResultPendingCount+:deltaOfPendingCount,
                t1.taskResultCanceledCount=t1.taskResultCanceledCount+:deltaOfCanceledCount,
                t1.taskResultSucceededCount=t1.taskResultSucceededCount+:deltaOfSucceededCount,
                t1.taskResultFailedCount=t1.taskResultFailedCount+:deltaOfFailedCount
               WHERE t1.id = :executionId
            """)
    void adjustTaskResultCounters(
            @Param("executionId") int executionId,
            @Param("deltaOfPendingCount") int deltaOfPendingCount,
            @Param("deltaOfCanceledCount") int deltaOfCanceledCount,
            @Param("deltaOfFailedCount") int deltaOfFailedCount,
            @Param("deltaOfSucceededCount") int deltaOfSucceededCount
    );


    @Modifying
    @Query("""
                DELETE
                FROM LizNeuralExecution t
                WHERE t.concept.id=:conceptId
            """)
    void deleteAllWhereConceptId(
            @Param("conceptId") int conceptId
    );


}