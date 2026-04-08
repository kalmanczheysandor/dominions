package hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.repository.training;



import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.entity.training.neural.LizNeuralConcept;
import hu.kalmancheysandor.applications.dominions.apis.util.uuid.UUIDRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface LizNeuralConceptRepository extends JpaRepository< LizNeuralConcept, String>, UUIDRepository< LizNeuralConcept, String> {

    public LizNeuralConcept findById(int  lizNeuralConceptId);

    public  LizNeuralConcept findByUuid(String  lizNeuralConceptUuid);

    public List< LizNeuralConcept> findAll();

    public void deleteById(int  lizNeuralConceptId);

    public void deleteByUuid(String  lizNeuralConceptUuid);

    public boolean existsById(int  lizNeuralConceptId);

    public boolean existsByUuid(String  lizNeuralConceptUuid);

    public  LizNeuralConcept save(LizNeuralConcept  lizNeuralConcept);

    @Query("SELECT t FROM  LizNeuralConcept t")
    List< LizNeuralConcept> listAll();

    @Query("SELECT t FROM  LizNeuralConcept t WHERE t.enabled=true")
    List<LizNeuralConcept> listAllEnabled();

    @Query("SELECT t FROM LizNeuralConcept t WHERE t.id = :paramExcludedId OR t.enabled=true")
    List<LizNeuralConcept> listAllEnabledWithOneExcludedId(@Param("paramExcludedId") int excludedId);

    @Query("SELECT t FROM LizNeuralConcept t WHERE t.id IN :excludedIdList OR t.enabled=true")
    List<LizNeuralConcept> listAllEnabledWithMultipleExcludedId(@Param("excludedIdList") List<Integer> excludedIdList);

    @Query("SELECT t FROM LizNeuralConcept t WHERE t.uuid = :paramExcludedUuid OR t.enabled=true")
    List<LizNeuralConcept> listAllEnabledWithOneExcludedUuid(@Param("paramExcludedUuid") String excludedUuid);

    @Query("SELECT t FROM LizNeuralConcept t WHERE t.id IN :excludedUuidList OR t.enabled=true")
    List<LizNeuralConcept> listAllEnabledWithMultipleExcludedUuid(@Param("excludedUuidList") List<String> excludedUuidList);






    @Query("SELECT CASE WHEN count(t) > 0 THEN true ELSE false END FROM LizNeuralConcept t WHERE t.name = :name")
    public boolean isNameReserved(@Param("name") String title);

    @Query("SELECT CASE WHEN count(t) > 0 THEN true ELSE false END FROM  LizNeuralConcept t WHERE t.name = :name and t.id<> :excludedId")
    public boolean isNameReserved(@Param("name") String title, @Param("excludedId") int excludedId);





    @Query("SELECT t.enabled FROM  LizNeuralConcept t WHERE t.id=:lizNeuralConceptId")
    boolean isEnabled(@Param("lizNeuralConceptId") int  lizNeuralConceptId);



    @Query("SELECT CASE WHEN " +
            "EXISTS (SELECT 1 FROM LizVariant t WHERE t.concept.id = :lizNeuralConceptId) " +
            "THEN TRUE ELSE FALSE END")
    boolean isReferencedElsewhere(@Param("lizNeuralConceptId") int  lizNeuralConceptId);
}


