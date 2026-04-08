package hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.repository.history;


import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.entity.history.LizHistoryScenario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Stream;

@Repository
public interface LizHistoryScenarioRepository extends JpaRepository<LizHistoryScenario, Integer> {
    @Query("""
            SELECT t
            FROM LizHistoryScenario t
            WHERE
            t.scenarioUuid = :scenarioUuid
            """)
    public LizHistoryScenario findByScenarioUuid(@Param("scenarioUuid") String scenarioUuid);

    public LizHistoryScenario findById(int id);

    public List<LizHistoryScenario> findAll();


    public void deleteById(int id);

//    public boolean existsByScenarioUuid(String scenarioUuid);

    public LizHistoryScenario save(LizHistoryScenario lizHistoryScenario);


    @Query("SELECT COUNT(t) FROM LizHistoryScenario t")
    int countAll();


    @Query("SELECT t FROM LizHistoryScenario t")
    List<LizHistoryScenario> listAll();

    @Query("SELECT t FROM LizHistoryScenario t")
    Stream<LizHistoryScenario> streamAll();




}
