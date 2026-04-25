package hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.repository.training.neural;


import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.entity.training.neural.LizNeuralTrainingTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Stream;

@Repository
public interface LizNeuralTrainingTaskRepository extends JpaRepository<LizNeuralTrainingTask, LizNeuralTrainingTask.PrimaryKey> {
    //    public LizNeuralTrainingTask findById(int lizTrainingTaskId);
    public List<LizNeuralTrainingTask> findAll();

    //    public void deleteById(int lizTrainingTaskId);
//    public boolean existsById(int lizTrainingTaskId);
    public LizNeuralTrainingTask save(LizNeuralTrainingTask lizNeuralTrainingTask);


    @Query("SELECT t FROM LizNeuralTrainingTask t WHERE t.conceptId= :conceptId and t.executionId= :executionId and t.scenarioId= :scenarioId and t.playerId= :playerId")
    LizNeuralTrainingTask findByCompositeKey(@Param("conceptId") int conceptId, @Param("executionId") int executionId, @Param("scenarioId") int scenarioId, @Param("playerId") int playerId);




//    // TODO: gyenge nev
//    @Query("SELECT t FROM LizNeuralTrainingTask t WHERE t.conceptId== :conceptId and t.executionId== :executionId and t.scenarioId== :scenarioId")
//    List<LizNeuralTrainingTask> listRowsAtScenario(@Param("conceptId") int conceptId, @Param("executionId") int executionId, @Param("scenarioId") int scenarioId, Pageable pageable);
//
//



    //    @Modifying
//    @Query("DELETE FROM LizNeuralTrainingTask t WHERE t.trainingId <= :trainingId")
//    void deleteAllWhereTrainingId(@Param("trainingId") int trainingId);
    
    
    /// /////////////////////////////////////////////////////////////////////////////////////////
    @Query("SELECT t FROM LizNeuralTrainingTask t WHERE t.conceptId= :conceptId and t.executionId= :executionId and t.taskPhase='CREATED'")
    Stream<LizNeuralTrainingTask> streamAllTaskInCreatedPhaseAtExecutionId(@Param("conceptId") int conceptId, @Param("executionId") int executionId);

    @Query("SELECT t FROM LizNeuralTrainingTask t WHERE t.conceptId= :conceptId and t.executionId= :executionId and t.taskPhase='QUEUED'")
    Stream<LizNeuralTrainingTask> streamAllTaskInQueuedPhaseAtExecutionId(@Param("conceptId") int conceptId, @Param("executionId") int executionId);

    @Query("SELECT t FROM LizNeuralTrainingTask t WHERE t.conceptId= :conceptId and t.executionId= :executionId and t.taskPhase='PAUSED'")
    Stream<LizNeuralTrainingTask> streamAllTaskInPausedPhaseAtExecutionId(@Param("conceptId") int conceptId, @Param("executionId") int executionId);

    @Query("SELECT t FROM LizNeuralTrainingTask t WHERE t.conceptId= :conceptId and t.executionId= :executionId and t.taskPhase!='FINISHED'")
    Stream<LizNeuralTrainingTask> streamAllTaskInUnfinishedPhasesAtExecutionId(@Param("conceptId") int conceptId, @Param("executionId") int executionId);



    @Modifying
    @Query("""
        DELETE
        FROM LizNeuralTrainingTask t
        WHERE t.conceptId=:conceptId
    """)
    void deleteAllWhereConceptId(
            @Param("conceptId") int conceptId
    );

}
