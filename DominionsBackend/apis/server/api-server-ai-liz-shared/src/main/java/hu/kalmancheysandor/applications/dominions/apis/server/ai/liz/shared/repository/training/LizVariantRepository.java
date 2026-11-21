package hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.repository.training;



import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.entity.training.LizVariant;
import hu.kalmancheysandor.applications.dominions.apis.util.uuid.UUIDRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface LizVariantRepository extends JpaRepository< LizVariant, String>, UUIDRepository< LizVariant, String> {

    public LizVariant findById(int  variantId);

    public  LizVariant findByUuid(String  variantUuid);

    public List< LizVariant> findAll();

    public void deleteById(int  variantId);

    public void deleteByUuid(String  variantUuid);

    public boolean existsById(int  variantId);

    public boolean existsByUuid(String  variantUuid);

    public  LizVariant save( LizVariant  variant);

    @Query("SELECT t FROM  LizVariant t")
    List< LizVariant> listAll();

    @Query("SELECT t FROM  LizVariant t WHERE t.enabled=true")
    List<LizVariant> listAllEnabled();

    @Query("SELECT t FROM LizVariant t WHERE t.id = :paramExcludedId OR t.enabled=true")
    List<LizVariant> listAllEnabledWithOneExcludedId(@Param("paramExcludedId") int excludedId);

    @Query("SELECT t FROM LizVariant t WHERE t.id IN :excludedIdList OR t.enabled=true")
    List<LizVariant> listAllEnabledWithMultipleExcludedId(@Param("excludedIdList") List<Integer> excludedIdList);

    @Query("SELECT t FROM LizVariant t WHERE t.uuid = :paramExcludedUuid OR t.enabled=true")
    List<LizVariant> listAllEnabledWithOneExcludedUuid(@Param("paramExcludedUuid") String excludedUuid);

    @Query("SELECT t FROM LizVariant t WHERE t.id IN :excludedUuidList OR t.enabled=true")
    List<LizVariant> listAllEnabledWithMultipleExcludedUuid(@Param("excludedUuidList") List<String> excludedUuidList);






    @Query("SELECT CASE WHEN count(t) > 0 THEN true ELSE false END FROM  LizVariant t WHERE t.name = :name")
    public boolean isNameReserved(@Param("name") String title);

    @Query("SELECT CASE WHEN count(t) > 0 THEN true ELSE false END FROM  LizVariant t WHERE t.name = :name and t.id<> :excludedId")
    public boolean isNameReserved(@Param("name") String title, @Param("excludedId") int excludedId);





    @Query("SELECT t.enabled FROM  LizVariant t WHERE t.id=:variantId")
    boolean isEnabled(@Param("variantId") int  variantId);



    @Query("SELECT CASE WHEN " +
            "EXISTS (SELECT 1 FROM LizCharacter t WHERE t.variant.id = :variantId) " +
            "THEN TRUE ELSE FALSE END")
    boolean isReferencedElsewhere(@Param("variantId") int  variantId);
}


