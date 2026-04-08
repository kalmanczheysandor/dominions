package hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.repository.training.neural;


import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.entity.training.neural.LizNeuralTraining;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LizNeuralTrainingRepository extends JpaRepository<LizNeuralTraining, LizNeuralTraining.PrimaryKey> {
//    public LizNeuralTraining findById(int lizTrainingId);
    public List<LizNeuralTraining> findAll();
//    public void deleteById(int lizTrainingId);
//    public boolean existsById(int lizTrainingId);
    public LizNeuralTraining save(LizNeuralTraining lizNeuralTraining);

//    @Modifying
//    @Query("DELETE FROM LizNeuralTraining t WHERE t.scenarioId <= :scenarioId")
//    void deleteAllWhereScenarioId(@Param("scenarioId") int scenarioId);
}
