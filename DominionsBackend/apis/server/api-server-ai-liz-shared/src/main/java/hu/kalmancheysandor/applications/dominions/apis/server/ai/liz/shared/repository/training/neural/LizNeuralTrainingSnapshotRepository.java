package hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.repository.training.neural;


import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.entity.training.neural.LizNeuralTrainingResult;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.entity.training.neural.LizNeuralTrainingSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Stream;

@Repository
public interface LizNeuralTrainingSnapshotRepository extends JpaRepository<LizNeuralTrainingSnapshot, LizNeuralTrainingSnapshot.PrimaryKey> {
    public List<LizNeuralTrainingSnapshot> findAll();

    public LizNeuralTrainingSnapshot save(LizNeuralTrainingSnapshot trainingSnapshot);

    @Query("SELECT t FROM LizNeuralTrainingSnapshot t WHERE t.playerId = :playerId")
    public List<LizNeuralTrainingSnapshot> listAllAtPlayerId(@Param("playerId") int playerId);

    @Query("SELECT t FROM LizNeuralTrainingSnapshot t WHERE t.scenarioId = :scenarioId")
    public List<LizNeuralTrainingSnapshot> listAllAtScenarioId(@Param("scenarioId") int scenarioId);

    @Query("""
        SELECT t
        FROM LizNeuralTrainingSnapshot t
        WHERE
            t.conceptId = :conceptId AND
            t.executionId = :executionId AND
            t.scenarioId = :scenarioId AND
            t.playerId = :playerId
        ORDER BY t.turn ASC
    """)
    List<LizNeuralTrainingSnapshot> listAllWhereConceptIdAndExecutionIdAndScenarioIdAndPlayerId(
            @Param("conceptId") int conceptId,
            @Param("executionId") int executionId,
            @Param("scenarioId") int scenarioId,
            @Param("playerId") int playerId
    );


    @Query("""
        SELECT t
        FROM LizNeuralTrainingSnapshot t
        WHERE
            t.conceptId = :conceptId AND
            t.executionId = :executionId AND
            t.scenarioId = :scenarioId AND
            t.playerId = :playerId
        ORDER BY t.turn ASC
    """)
    Stream<LizNeuralTrainingSnapshot> streamAllWhereConceptIdAndExecutionIdAndScenarioIdAndPlayerId(
            @Param("conceptId") int conceptId,
            @Param("executionId") int executionId,
            @Param("scenarioId") int scenarioId,
            @Param("playerId") int playerId
    );

    @Query("""
    SELECT COUNT(t)
    FROM LizNeuralTrainingSnapshot t
    WHERE
        t.conceptId = :conceptId AND
        t.executionId = :executionId AND
        t.scenarioId = :scenarioId AND
        t.playerId = :playerId
""")
    long countAllWhereConceptIdAndExecutionIdAndScenarioIdAndPlayerId(
            @Param("conceptId") int conceptId,
            @Param("executionId") int executionId,
            @Param("scenarioId") int scenarioId,
            @Param("playerId") int playerId
    );

//    @Query("SELECT t FROM LizNeuralTrainingSnapshot t WHERE t.trainingId = :trainingId")
//    public List<LizNeuralTrainingSnapshot> listAllAtTrainingId(@Param("trainingId") int trainingId);
//
//    @Query("SELECT t FROM LizNeuralTrainingSnapshot t WHERE  t.scenarioId = :scenarioId AND t.trainingId = :trainingId AND t.playerId = :playerId ORDER BY t.turn")
//    public List<LizNeuralTrainingSnapshot> listAllAtScenarioIdAndTrainingIdAndPlayerId(@Param("scenarioId") int scenarioId, @Param("trainingId") int trainingId, @Param("playerId") int playerId);

//    @Modifying
//    @Query("DELETE FROM LizNeuralTrainingSnapshot t WHERE t.scenarioId <= :scenarioId")
//    void deleteAllWhereScenarioId(@Param("scenarioId") int scenarioId);

    @Modifying
    @Query("""
        DELETE
        FROM LizNeuralTrainingSnapshot t
        WHERE t.conceptId=:conceptId and t.executionId=:executionId and t.scenarioId=:scenarioId and t.playerId=:playerId
    """)
    void deleteAllWhereConceptIdAndExecutionIdAndScenarioIdAndPlayerId(
            @Param("conceptId") int conceptId,
            @Param("executionId") int executionId,
            @Param("scenarioId") int scenarioId,
            @Param("playerId") int playerId
    );



    @Modifying
    @Query("""
        DELETE
        FROM LizNeuralTrainingSnapshot t
        WHERE t.conceptId=:conceptId
    """)
    void deleteAllWhereConceptId(
            @Param("conceptId") int conceptId
    );
}
