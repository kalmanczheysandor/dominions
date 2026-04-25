package hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.repository.history;


import hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.entity.history.HugoHistoryScenario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Stream;

@Repository
public interface HugoHistoryScenarioRepository extends JpaRepository<HugoHistoryScenario, Integer> {
    @Query("""
            SELECT t
            FROM HugoHistoryScenario t
            WHERE
            t.scenarioUuid = :scenarioUuid
            """)
    public HugoHistoryScenario findByScenarioUuid(@Param("scenarioUuid") String scenarioUuid);

    public HugoHistoryScenario findById(int id);

    public List<HugoHistoryScenario> findAll();


    public void deleteById(int id);

//    public boolean existsByScenarioUuid(String scenarioUuid);

    public HugoHistoryScenario save(HugoHistoryScenario hugoHistoryScenario);


    @Query("SELECT COUNT(t) FROM HugoHistoryScenario t")
    int countAll();


    @Query("SELECT t FROM HugoHistoryScenario t")
    List<HugoHistoryScenario> listAll();

    @Query("SELECT t FROM HugoHistoryScenario t")
    Stream<HugoHistoryScenario> streamAll();




}
