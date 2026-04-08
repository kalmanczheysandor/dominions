package hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.repository.training.neural;


import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.entity.history.LizHistoryPlayer;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.entity.history.LizHistoryScenario;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.entity.training.neural.LizNeuralTrainingResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Stream;

@Repository
public interface LizNeuralTrainingResultRepository extends JpaRepository<LizNeuralTrainingResult, LizNeuralTrainingResult.PrimaryKey> {

    public List<LizNeuralTrainingResult> findAll();

    public LizNeuralTrainingResult save(LizNeuralTrainingResult trainingLog);

    @Query("SELECT t FROM LizNeuralTrainingResult t WHERE t.playerId = :playerId")
    public List<LizNeuralTrainingResult> listAllAtPlayerId(@Param("playerId") int playerId);

    @Query("""
            SELECT t
            FROM LizNeuralTrainingResult t
            WHERE
            t.conceptId = :conceptId AND
            t.executionId = :executionId AND
            t.scenarioId = :scenarioId AND
            t.playerId = :playerId
            """)
    public LizNeuralTrainingResult findAScenarioResultOfAPlayer(@Param("conceptId") int conceptId,@Param("executionId") int executionId,@Param("scenarioId") int scenarioId,@Param("playerId") int playerId);

    @Query("SELECT t FROM LizNeuralTrainingResult t WHERE t.scenarioId = :scenarioId")
    public List<LizNeuralTrainingResult> listAllAtScenarioId(@Param("scenarioId") int scenarioId);

    @Query("""
    SELECT DISTINCT s
    FROM LizHistoryScenario s
    JOIN LizNeuralTrainingResult r ON s.id = r.scenarioId
    WHERE
      r.conceptId = :conceptId AND
      r.executionId = :executionId
    """)
    List<LizHistoryScenario> collectAllHistoryScenario(@Param("conceptId") int conceptId, @Param("executionId") int executionId);

    @Query("""
    SELECT DISTINCT p
    FROM LizHistoryPlayer p
    JOIN LizNeuralTrainingResult r ON p.id = r.playerId
    WHERE
      r.conceptId = :conceptId AND
      r.executionId = :executionId AND
      r.scenarioId = :scenarioId
    """)
    List<LizHistoryPlayer> collectAllHistoryPlayer(@Param("conceptId") int conceptId, @Param("executionId") int executionId,@Param("scenarioId") int scenarioId);


//    @Query("SELECT t FROM LizNeuralTrainingResult t WHERE t.trainingId = :trainingId")
//    public List<LizTrainingSnapshot> listAllAtTrainingId(@Param("trainingId") int trainingId);
//
//    @Query("SELECT t FROM LizNeuralTrainingResult t WHERE  t.scenarioId = :scenarioId AND t.trainingId = :trainingId AND t.playerId = :playerId")
//    public  List<LizTrainingSnapshot> listAllAtScenarioIdAndTrainingIdAndPlayerId(@Param("scenarioId") int scenarioId, @Param("trainingId") int trainingId, @Param("playerId") int playerId);
//
//    @Modifying
//    @Query("DELETE FROM LizNeuralTrainingResult t WHERE t.scenarioId <= :scenarioId")
//    void deleteAllWhereScenarioId(@Param("scenarioId") int scenarioId);

    @Query("""
            SELECT t
            FROM LizNeuralTrainingResult t
            WHERE
            t.conceptId = :conceptId AND
            t.scenarioId = :scenarioId AND
            t.playerId = :playerId
            """)
    public Stream<LizNeuralTrainingResult> streamResultOfAllExecution(
        @Param("conceptId") int conceptId,
        @Param("scenarioId") int scenarioId,
        @Param("playerId") int playerId
    );

}
