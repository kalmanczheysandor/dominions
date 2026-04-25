package hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.repository.history;


import hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.entity.history.HugoHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HugoHistoryRepository extends JpaRepository<HugoHistory, HugoHistory.PrimaryKey> {
//    public LizHistory findBy(long userId);
    public List<HugoHistory> findAll();
//
//        public void deleteById(long userId);
//        public boolean existsById(long userId);
    public HugoHistory save(HugoHistory history);


    @Query("SELECT t FROM HugoHistory t WHERE t.playerId = :playerId")
    List<HugoHistory> listAllAtPlayerId(@Param("playerId") int playerId);

    @Query("SELECT t FROM HugoHistory t WHERE t.playerId = :playerId AND t.scenarioId = :scenarioId ORDER BY t.dateCreated DESC ")
    List<HugoHistory> listAllAtPlayerIdAndScenarioId(@Param("playerId") int playerId, @Param("scenarioId") int scenarioId);

    @Modifying
    @Query("DELETE FROM HugoHistory t WHERE t.scenarioId <= :scenarioId")
    void deleteAllWhereScenarioId(@Param("scenarioId") int scenarioId);
}
