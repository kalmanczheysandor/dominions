package hu.kalmancheysandor.applications.dominions.apis.server.game.common.repository.game;


import hu.kalmancheysandor.applications.dominions.apis.server.game.common.entity.game.GameScenario;
import hu.kalmancheysandor.applications.dominions.apis.server.game.common.entity.game.GameScenario;
import hu.kalmancheysandor.applications.dominions.apis.util.uuid.UUIDRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface GameScenarioRepository extends JpaRepository<GameScenario, String>, UUIDRepository<GameScenario, String> {

    public GameScenario findById(int gameScenarioId);

    public GameScenario findByUuid(String gameScenarioUuid);

    public List<GameScenario> findAll();

    public void deleteById(int gameScenarioId);

    public void deleteByUuid(String gameScenarioUuid);

    public boolean existsById(int gameScenarioId);

    public boolean existsByUuid(String gameScenarioUuid);

    public GameScenario save(GameScenario gameScenario);

    @Query("SELECT b FROM GameScenario b WHERE b.enabled=true")
    List<GameScenario> listAllEnabled();

    @Query("SELECT b FROM GameScenario b WHERE b.published=true and b.enabled=true ORDER BY b.title ASC")
    List<GameScenario> listAllPublished();

    @Query("SELECT b FROM GameScenario b")
    List<GameScenario> listAll();


    @Query("SELECT CASE WHEN count(t) > 0 THEN true ELSE false END FROM GameScenario t WHERE t.title = :title")
    public boolean isTitleReserved(@Param("title") String title);

    @Query("SELECT CASE WHEN count(t) > 0 THEN true ELSE false END FROM GameScenario t WHERE t.title = :title and t.id<> :excludedId")
    public boolean isTitleReserved(@Param("title") String title, @Param("excludedId") int excludedId);

    @Query("SELECT t.enabled FROM GameScenario t WHERE t.id=:gameScenarioId")
    boolean isEnabled(@Param("gameScenarioId") int gameScenarioId);

    @Query("SELECT t.published FROM GameScenario t WHERE t.id=:gameScenarioId")
    boolean isPublished(@Param("gameScenarioId") int gameScenarioId);

    @Query("SELECT CASE WHEN " +
            "EXISTS (SELECT 1 FROM GameSession t WHERE t.scenarioId = :gameScenarioId) " +
            "THEN TRUE ELSE FALSE END")
    boolean isReferencedElsewhere(@Param("gameScenarioId") int gameScenarioId);

}


