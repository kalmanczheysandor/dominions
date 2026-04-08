package hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.repository.training.neural;


import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.entity.training.neural.LizNeuralTrainingResultLatest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface LizNeuralTrainingResultLatestRepository extends JpaRepository<LizNeuralTrainingResultLatest, LizNeuralTrainingResultLatest.PrimaryKey> {
    public List<LizNeuralTrainingResultLatest> findAll();

    @Query("SELECT t FROM LizNeuralTrainingResultLatest t WHERE t.playerId = :playerId AND t.scenarioId = :scenarioId")
    public LizNeuralTrainingResultLatest findLatest(@Param("scenarioId") int scenarioId, @Param("playerId") int playerId);
}
