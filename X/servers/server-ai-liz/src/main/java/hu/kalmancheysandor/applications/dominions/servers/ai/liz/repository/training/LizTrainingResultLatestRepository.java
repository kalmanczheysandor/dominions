package hu.kalmancheysandor.applications.dominions.servers.ai.liz.repository.training;


import hu.kalmancheysandor.applications.dominions.servers.ai.liz.entity.training.LizTrainingResultLatest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface LizTrainingResultLatestRepository extends JpaRepository<LizTrainingResultLatest, LizTrainingResultLatest.PrimaryKey> {
    public List<LizTrainingResultLatest> findAll();

    @Query("SELECT t FROM LizTrainingResultLatest t WHERE t.playerId = :playerId AND t.scenarioId = :scenarioId")
    public LizTrainingResultLatest findLatest(@Param("scenarioId") int scenarioId, @Param("playerId") int playerId);
}
