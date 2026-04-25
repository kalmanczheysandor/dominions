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
import java.util.stream.Stream;


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

//    @Query("SELECT t FROM  LizNeuralExecution t WHERE t.applicable =true")
//    List<LizNeuralExecution> listAllEnabled();
//
//    @Query("SELECT t FROM LizNeuralExecution t WHERE t.id = :paramExcludedId OR t.applicable=true")
//    List<LizNeuralExecution> listAllEnabledWithOneExcludedId(@Param("paramExcludedId") int excludedId);
//
//    @Query("SELECT t FROM LizNeuralExecution t WHERE t.id IN :excludedIdList OR t.applicable=true")
//    List<LizNeuralExecution> listAllEnabledWithMultipleExcludedId(@Param("excludedIdList") List<Integer> excludedIdList);
//
//    @Query("SELECT t FROM LizNeuralExecution t WHERE t.uuid = :paramExcludedUuid OR t.applicable=true")
//    List<LizNeuralExecution> listAllEnabledWithOneExcludedUuid(@Param("paramExcludedUuid") String excludedUuid);
//
//    @Query("SELECT t FROM LizNeuralExecution t WHERE t.id IN :excludedUuidList OR t.applicable=true")
//    List<LizNeuralExecution> listAllEnabledWithMultipleExcludedUuid(@Param("excludedUuidList") List<String> excludedUuidList);
//
//
//    @Query("SELECT CASE WHEN count(t) > 0 THEN true ELSE false END FROM  LizNeuralExecution t WHERE t.name = :name")
//    public boolean isNameReserved(@Param("name") String title);
//
//    @Query("SELECT CASE WHEN count(t) > 0 THEN true ELSE false END FROM  LizNeuralExecution t WHERE t.name = :name and t.id<> :excludedId")
//    public boolean isNameReserved(@Param("name") String title, @Param("excludedId") int excludedId);


//    @Query("SELECT t.applicable FROM  LizNeuralExecution t WHERE t.id=:lizNeuralExecutionId")
//    boolean isApplicable(@Param("lizNeuralExecutionId") int lizNeuralExecutionId);

//
//    @Query("SELECT CASE WHEN " +
//            "EXISTS (SELECT 1 FROM LizCharacter t WHERE t.variant.id = :lizNeuralExecutionId) " +
//            "THEN TRUE ELSE FALSE END")
//    boolean isReferencedElsewhere(@Param("lizNeuralExecutionId") int lizNeuralExecutionId);


//    @Lock(LockModeType.PESSIMISTIC_WRITE)   // treated as ... SELECT ....FOR UPDATE
//    @Query("SELECT t FROM LizNeuralExecution t WHERE t.processPhase!='FINISHED'")
//    @QueryHints({
//            @QueryHint(name = "jakarta.persistence.lock.timeout", value = "0")  // NO WAIT
//    })
//    List<LizNeuralTrainingTask> findUnfinished(@Param("conceptId") int conceptId);
//
//
//    @Lock(LockModeType.PESSIMISTIC_WRITE) // treated as: SELECT ....FOR UPDATE
//    @Query("""
//            SELECT t FROM LizNeuralExecution t
//            WHERE t.processPhase <> 'FINISHED'
//              AND t.concept.id = :conceptId
//            """)
//    @QueryHints({
//            @QueryHint(name = "jakarta.persistence.lock.timeout", value = "10000") // 10000 milisecond = 10 sec
//    })
//    List<LizNeuralExecution> findAllUnfinishedWithExclusiveLock(@Param("conceptId") int conceptId);


//    @Lock(LockModeType.PESSIMISTIC_WRITE) // treated as: SELECT ....FOR UPDATE
//    @Query("""
//            SELECT t FROM LizNeuralExecution t
//            WHERE t.processPhase <> 'FINISHED'
//              AND t.concept.id = :conceptId
//            """)
//    @QueryHints({
//            @QueryHint(name = "jakarta.persistence.lock.timeout", value = "10000") // 10000 milisecond = 10 sec
//    })
//    LizNeuralExecution findTheUnfinishedWithExclusiveLock(@Param("conceptId") int conceptId);


    @Lock(LockModeType.PESSIMISTIC_WRITE) // treated as: SELECT ....FOR UPDATE
    @Query("SELECT t FROM LizNeuralExecution t WHERE t.id = :executionId")
    @QueryHints({
            @QueryHint(name = "jakarta.persistence.lock.timeout", value = "10000") // 10000 milisecond = 10 sec
    })
    LizNeuralExecution accessByIdWithExclusiveLock(@Param("executionId") int executionId);


//    @Query("""
//            SELECT
//                t1.id as id,
//                t1.uuid as uuid,
//                t1.name as name,
//                t1.concept as concept,
//                t1.processPhase as processPhase,
//                t1.processState as processState,
//                t1.applicable as applicable,
//                t1.dateCreated as dateCreated,
//                t1.dateModified as dateModified,
//                (select count(t2) from LizNeuralTrainingTask t2 where t2.buildId = t1.id)  as taskAllCount,
//                (select count(t2) from LizNeuralTrainingTask t2 where t2.buildId = t1.id and t2.taskPhase = 'FINISHED')  as taskFinishedCount,
//                (select count(t2) from LizNeuralTrainingTask t2 where t2.buildId = t1.id and t2.taskPhase = 'FINISHED' and t2.taskResult='FAILED')  as taskFailedCount,
//                (select count(t2) from LizNeuralTrainingTask t2 where t2.buildId = t1.id and t2.taskPhase = 'FINISHED' and t2.taskResult='SUCCEEDED')  as taskSucceedCount
//            FROM  LizNeuralExecution t1
//            WHERE t1.concept.id =:conceptId
//            """)
//    LizNeuralExecution.ExtendedView findExtendedAtConceptId(@Param("conceptId") int  conceptId);
//
//    int concept(LizNeuralConcept concept);


    /// //////////////////////////////////////////////////////
    /// //////////////////////////////////////////////////////
    /// //////////////////////////////////////////////////////

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








//    /**
//     *   !!!! Ez az adatbazis allapota szerint jar el, ezert fontos hogy minden entitast elobb ki flussoljunk a persistance contextbol, hogy szinkronban legyen a memoria es a db
//     * @param executionId
//     * @return
//     */
//    @Modifying(clearAutomatically = true, flushAutomatically = true)
//    @Query("""
//            UPDATE LizNeuralExecution t1
//            SET t1.processPhase='FINISHED'
//            WHERE t1.id = :executionId AND NOT EXISTS (
//                  SELECT 1
//                  FROM LizNeuralTrainingTask t2
//                  WHERE t2.executionId = t1.id AND t2.taskPhase != 'FINISHED'
//                          )
//            """)
//    int finishExecutionIfAllTaskFinished(@Param("executionId") int executionId);
//


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


//    @Modifying
//    @Query("""
//               UPDATE LizNeuralExecution t1
//               SET t1.taskAllCount=t1.taskAllCount+1,
//                   t1.taskPhaseCreatedCount=t1.taskPhaseCreatedCount+1
//               WHERE t1.id = :executionId
//            """)
//    void adjustTaskPhaseCountOnCreated(@Param("executionId") int executionId);
//
//
//    @Modifying
//    @Query("""
//               UPDATE LizNeuralExecution t1
//               SET
//                   t1.taskPhaseCreatedCount=t1.taskPhaseCreatedCount-1,
//                   t1.taskPhaseQueuedCount=t1.taskPhaseQueuedCount+1
//               WHERE t1.id = :executionId and t1.taskPhaseCreatedCount >0
//            """)
//    void adjustTaskPhaseCountOnCreatedToQueued(@Param("executionId") int executionId);
//
//
//    @Modifying
//    @Query("""
//               UPDATE LizNeuralExecution t1
//               SET
//                   t1.taskPhaseQueuedCount=t1.taskPhaseQueuedCount-1,
//                   t1.taskPhasePausedCount=t1.taskPhasePausedCount+1
//               WHERE t1.id = :executionId and t1.taskPhaseQueuedCount >0
//            """)
//    void adjustTaskPhaseCountOnQueuedToPaused(@Param("executionId") int executionId);
//
//    @Modifying
//    @Query("""
//               UPDATE LizNeuralExecution t1
//               SET
//                   t1.taskPhasePausedCount=t1.taskPhasePausedCount-1,
//                   t1.taskPhaseQueuedCount=t1.taskPhaseQueuedCount+1
//               WHERE t1.id = :executionId and t1.taskPhasePausedCount >0
//            """)
//    void adjustTaskPhaseCountOnPausedToQueued(@Param("executionId") int executionId);
//
//
//    @Modifying
//    @Query("""
//               UPDATE LizNeuralExecution t1
//               SET
//                   t1.taskPhaseQueuedCount=t1.taskPhaseQueuedCount-1,
//                   t1.taskPhaseFinishedCount=t1.taskPhaseFinishedCount+1
//               WHERE t1.id = :executionId  and t1.taskPhaseQueuedCount >0
//            """)
//    void adjustTaskPhaseCountOnQueuedToFinished(@Param("executionId") int executionId);
//



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





//
//
//
//    @Modifying
//    @Query("""
//               UPDATE LizNeuralExecution t1
//               SET
//                   t1.taskResultPendingCount=t1.taskResultPendingCount+1
//               WHERE t1.id = :executionId
//            """)
//    void adjustTaskResultCountOnNothingToPending(@Param("executionId") int executionId);
//
//
//    @Modifying
//    @Query("""
//               UPDATE LizNeuralExecution t1
//               SET
//                   t1.taskResultPendingCount=t1.taskResultPendingCount-1,
//                   t1.taskResultFailedCount=t1.taskResultFailedCount+1
//               WHERE t1.id = :executionId  and t1.taskResultPendingCount >0
//            """)
//    void adjustTaskResultCountOnPendingToFailed(@Param("executionId") int executionId);
//
//
//    @Modifying
//    @Query("""
//               UPDATE LizNeuralExecution t1
//               SET
//                   t1.taskResultPendingCount=t1.taskResultPendingCount-1,
//                   t1.taskResultSucceededCount=t1.taskResultSucceededCount+1
//               WHERE t1.id = :executionId and t1.taskResultPendingCount >0
//            """)
//    void adjustTaskResultCountOnPendingToSucceeded(@Param("executionId") int executionId);
//
//
//    @Modifying
//    @Query("""
//               UPDATE LizNeuralExecution t1
//               SET
//                   t1.taskResultPendingCount=t1.taskResultPendingCount-1,
//                   t1.taskResultCanceledCount=t1.taskResultCanceledCount+1
//               WHERE t1.id = :executionId and t1.taskResultPendingCount >0
//            """)
//    void adjustTaskResultCountOnPendingToCanceled(@Param("executionId") int executionId);
//





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