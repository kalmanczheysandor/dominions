package hu.kalmancheysandor.applications.dominions.servers.ai.liz.repository.training;


import hu.kalmancheysandor.applications.dominions.apis.util.uuid.UUIDRepository;
import hu.kalmancheysandor.applications.dominions.servers.ai.liz.entity.training.LizTraining;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LizTrainingRepository extends JpaRepository<LizTraining, Integer>, UUIDRepository<LizTraining, Integer> {
    public LizTraining findById(int lizTrainingId);
    public List<LizTraining> findAll();
    public void deleteById(int lizTrainingId);
    public boolean existsById(int lizTrainingId);
    public LizTraining save(LizTraining lizTraining);

    @Modifying
    @Query("DELETE FROM LizTraining t WHERE t.scenarioId <= :scenarioId")
    void deleteAllWhereScenarioId(@Param("scenarioId") int scenarioId);
}
