package hu.kalmancheysandor.applications.dominions.apis.server.ai.otto.shared.repository.history;


import hu.kalmancheysandor.applications.dominions.apis.server.ai.otto.shared.entity.history.OttoHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OttoHistoryRepository extends JpaRepository<OttoHistory, OttoHistory.PrimaryKey> {

    public List<OttoHistory> findAll();

    public OttoHistory save(OttoHistory history);

    @Query("SELECT t FROM  OttoHistory t WHERE t.playerId = :playerId")
    List<OttoHistory> listAllAtPlayerId(@Param("playerId") int playerId);

    @Query("SELECT t FROM  OttoHistory t WHERE t.playerId = :playerId AND t.scenarioId = :scenarioId ORDER BY t.dateCreated DESC ")
    List<OttoHistory> listAllAtPlayerIdAndScenarioId(@Param("playerId") int playerId, @Param("scenarioId") int scenarioId);

    @Modifying
    @Query("DELETE FROM  OttoHistory t WHERE t.scenarioId <= :scenarioId")
    void deleteAllWhereScenarioId(@Param("scenarioId") int scenarioId);
}
