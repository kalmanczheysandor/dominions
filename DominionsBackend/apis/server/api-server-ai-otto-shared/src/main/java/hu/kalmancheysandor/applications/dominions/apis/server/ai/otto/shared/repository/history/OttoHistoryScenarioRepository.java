package hu.kalmancheysandor.applications.dominions.apis.server.ai.otto.shared.repository.history;


import hu.kalmancheysandor.applications.dominions.apis.server.ai.otto.shared.entity.history.OttoHistoryScenario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Stream;

@Repository
public interface OttoHistoryScenarioRepository extends JpaRepository<OttoHistoryScenario, Integer> {
    @Query("""
            SELECT t
            FROM  OttoHistoryScenario t
            WHERE
            t.scenarioUuid = :scenarioUuid
            """)
    public OttoHistoryScenario findByScenarioUuid(@Param("scenarioUuid") String scenarioUuid);

    public OttoHistoryScenario findById(int id);

    public List<OttoHistoryScenario> findAll();

    public void deleteById(int id);

    public OttoHistoryScenario save(OttoHistoryScenario historyScenario);

    @Query("SELECT COUNT(t) FROM  OttoHistoryScenario t")
    int countAll();

    @Query("SELECT t FROM  OttoHistoryScenario t")
    List<OttoHistoryScenario> listAll();

    @Query("SELECT t FROM  OttoHistoryScenario t")
    Stream<OttoHistoryScenario> streamAll();


}
