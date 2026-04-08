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

    public LizVariant findById(int  lizVariantId);

    public  LizVariant findByUuid(String  lizVariantUuid);

    public List< LizVariant> findAll();

    public void deleteById(int  lizVariantId);

    public void deleteByUuid(String  lizVariantUuid);

    public boolean existsById(int  lizVariantId);

    public boolean existsByUuid(String  lizVariantUuid);

    public  LizVariant save( LizVariant  lizVariant);

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





    @Query("SELECT t.enabled FROM  LizVariant t WHERE t.id=:lizVariantId")
    boolean isEnabled(@Param("lizVariantId") int  lizVariantId);



    @Query("SELECT CASE WHEN " +
            "EXISTS (SELECT 1 FROM LizCharacter t WHERE t.variant.id = :lizVariantId) " +
            "THEN TRUE ELSE FALSE END")
    boolean isReferencedElsewhere(@Param("lizVariantId") int  lizVariantId);
}


