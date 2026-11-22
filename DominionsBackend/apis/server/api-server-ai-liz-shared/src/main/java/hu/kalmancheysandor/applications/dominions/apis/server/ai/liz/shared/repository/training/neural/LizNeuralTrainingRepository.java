package hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.repository.training.neural;


import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.entity.training.neural.LizNeuralTraining;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LizNeuralTrainingRepository extends JpaRepository<LizNeuralTraining, LizNeuralTraining.PrimaryKey> {
    public List<LizNeuralTraining> findAll();

    public LizNeuralTraining save(LizNeuralTraining lizNeuralTraining);

    @Modifying
    @Query("""
                DELETE
                FROM LizNeuralTraining t
                WHERE t.conceptId=:conceptId
            """)
    void deleteAllWhereConceptId(
            @Param("conceptId") int conceptId
    );
}
