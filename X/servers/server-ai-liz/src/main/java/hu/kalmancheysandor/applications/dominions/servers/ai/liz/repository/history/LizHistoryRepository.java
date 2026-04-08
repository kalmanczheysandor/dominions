package hu.kalmancheysandor.applications.dominions.servers.ai.liz.repository.history;

import hu.kalmancheysandor.applications.dominions.servers.ai.liz.entity.history.LizHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface LizHistoryRepository extends JpaRepository<LizHistory, LizHistory.PrimaryKey> {
//    public LizHistory findBy(long userId);
    public List<LizHistory> findAll();
//
//        public void deleteById(long userId);
//        public boolean existsById(long userId);
    public LizHistory save(LizHistory history);


    @Query("SELECT t FROM LizHistory t WHERE t.playerId = :playerId")
    List<LizHistory> listAllAtPlayerId(@Param("playerId") int playerId);

    @Query("SELECT t FROM LizHistory t WHERE t.playerId = :playerId AND t.scenarioId = :scenarioId ORDER BY t.dateCreated DESC ")
    List<LizHistory> listAllAtPlayerIdAndScenarioId(@Param("playerId") int playerId,@Param("scenarioId") int scenarioId);

    @Modifying
    @Query("DELETE FROM LizHistory t WHERE t.scenarioId <= :scenarioId")
    void deleteAllWhereScenarioId(@Param("scenarioId") int scenarioId);
}
