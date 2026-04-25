package hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.repository.training;



import hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.entity.training.HugoVariant;
import hu.kalmancheysandor.applications.dominions.apis.util.uuid.UUIDRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface HugoVariantRepository extends JpaRepository<HugoVariant, String>, UUIDRepository<HugoVariant, String> {

    public HugoVariant findById(int  lizVariantId);

    public HugoVariant findByUuid(String  lizVariantUuid);

    public List<HugoVariant> findAll();

    public void deleteById(int  lizVariantId);

    public void deleteByUuid(String  lizVariantUuid);

    public boolean existsById(int  lizVariantId);

    public boolean existsByUuid(String  lizVariantUuid);

    public HugoVariant save(HugoVariant hugoVariant);

    @Query("SELECT t FROM  HugoVariant t")
    List<HugoVariant> listAll();

    @Query("SELECT t FROM  HugoVariant t WHERE t.enabled=true")
    List<HugoVariant> listAllEnabled();

    @Query("SELECT t FROM HugoVariant t WHERE t.id = :paramExcludedId OR t.enabled=true")
    List<HugoVariant> listAllEnabledWithOneExcludedId(@Param("paramExcludedId") int excludedId);

    @Query("SELECT t FROM HugoVariant t WHERE t.id IN :excludedIdList OR t.enabled=true")
    List<HugoVariant> listAllEnabledWithMultipleExcludedId(@Param("excludedIdList") List<Integer> excludedIdList);

    @Query("SELECT t FROM HugoVariant t WHERE t.uuid = :paramExcludedUuid OR t.enabled=true")
    List<HugoVariant> listAllEnabledWithOneExcludedUuid(@Param("paramExcludedUuid") String excludedUuid);

    @Query("SELECT t FROM HugoVariant t WHERE t.id IN :excludedUuidList OR t.enabled=true")
    List<HugoVariant> listAllEnabledWithMultipleExcludedUuid(@Param("excludedUuidList") List<String> excludedUuidList);






    @Query("SELECT CASE WHEN count(t) > 0 THEN true ELSE false END FROM  HugoVariant t WHERE t.name = :name")
    public boolean isNameReserved(@Param("name") String title);

    @Query("SELECT CASE WHEN count(t) > 0 THEN true ELSE false END FROM  HugoVariant t WHERE t.name = :name and t.id<> :excludedId")
    public boolean isNameReserved(@Param("name") String title, @Param("excludedId") int excludedId);





    @Query("SELECT t.enabled FROM  HugoVariant t WHERE t.id=:lizVariantId")
    boolean isEnabled(@Param("lizVariantId") int  lizVariantId);



    @Query("SELECT CASE WHEN " +
            "EXISTS (SELECT 1 FROM HugoCharacter t WHERE t.variant.id = :lizVariantId) " +
            "THEN TRUE ELSE FALSE END")
    boolean isReferencedElsewhere(@Param("lizVariantId") int  lizVariantId);
}


