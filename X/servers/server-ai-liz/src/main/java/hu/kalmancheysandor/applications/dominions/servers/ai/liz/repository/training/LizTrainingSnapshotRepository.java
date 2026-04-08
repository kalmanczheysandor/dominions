package hu.kalmancheysandor.applications.dominions.servers.ai.liz.repository.training;


import hu.kalmancheysandor.applications.dominions.servers.ai.liz.entity.training.LizTrainingSnapshot;
import hu.kalmancheysandor.applications.dominions.servers.ai.liz.entity.training.LizTrainingSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LizTrainingSnapshotRepository extends JpaRepository<LizTrainingSnapshot, LizTrainingSnapshot.PrimaryKey> {
    public List<LizTrainingSnapshot> findAll();

    public LizTrainingSnapshot save(LizTrainingSnapshot trainingSnapshot);

    @Query("SELECT t FROM LizTrainingSnapshot t WHERE t.playerId = :playerId")
    public List<LizTrainingSnapshot> listAllAtPlayerId(@Param("playerId") int playerId);

    @Query("SELECT t FROM LizTrainingSnapshot t WHERE t.scenarioId = :scenarioId")
    public List<LizTrainingSnapshot> listAllAtScenarioId(@Param("scenarioId") int scenarioId);

    @Query("SELECT t FROM LizTrainingSnapshot t WHERE t.trainingId = :trainingId")
    public List<LizTrainingSnapshot> listAllAtTrainingId(@Param("trainingId") int trainingId);

    @Query("SELECT t FROM LizTrainingSnapshot t WHERE  t.scenarioId = :scenarioId AND t.trainingId = :trainingId AND t.playerId = :playerId ORDER BY t.turn")
    public List<LizTrainingSnapshot> listAllAtScenarioIdAndTrainingIdAndPlayerId(@Param("scenarioId") int scenarioId, @Param("trainingId") int trainingId, @Param("playerId") int playerId);

    @Modifying
    @Query("DELETE FROM LizTrainingSnapshot t WHERE t.scenarioId <= :scenarioId")
    void deleteAllWhereScenarioId(@Param("scenarioId") int scenarioId);
}
