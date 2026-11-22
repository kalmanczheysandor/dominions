package hu.kalmancheysandor.applications.dominions.apis.server.ai.otto.shared.repository.training;


import hu.kalmancheysandor.applications.dominions.apis.server.ai.otto.shared.entity.training.OttoVariant;
import hu.kalmancheysandor.applications.dominions.apis.util.uuid.UUIDRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface OttoVariantRepository extends JpaRepository<OttoVariant, String>, UUIDRepository<OttoVariant, String> {

    public OttoVariant findById(int variantId);

    public OttoVariant findByUuid(String variantUuid);

    public List<OttoVariant> findAll();

    public void deleteById(int variantId);

    public void deleteByUuid(String variantUuid);

    public boolean existsById(int variantId);

    public boolean existsByUuid(String variantUuid);

    public OttoVariant save(OttoVariant variant);

    @Query("SELECT t FROM  OttoVariant t")
    List<OttoVariant> listAll();

    @Query("SELECT t FROM  OttoVariant t WHERE t.enabled=true")
    List<OttoVariant> listAllEnabled();

    @Query("SELECT t FROM OttoVariant t WHERE t.id = :paramExcludedId OR t.enabled=true")
    List<OttoVariant> listAllEnabledWithOneExcludedId(@Param("paramExcludedId") int excludedId);

    @Query("SELECT t FROM OttoVariant t WHERE t.id IN :excludedIdList OR t.enabled=true")
    List<OttoVariant> listAllEnabledWithMultipleExcludedId(@Param("excludedIdList") List<Integer> excludedIdList);

    @Query("SELECT t FROM OttoVariant t WHERE t.uuid = :paramExcludedUuid OR t.enabled=true")
    List<OttoVariant> listAllEnabledWithOneExcludedUuid(@Param("paramExcludedUuid") String excludedUuid);

    @Query("SELECT t FROM OttoVariant t WHERE t.id IN :excludedUuidList OR t.enabled=true")
    List<OttoVariant> listAllEnabledWithMultipleExcludedUuid(@Param("excludedUuidList") List<String> excludedUuidList);


    @Query("SELECT CASE WHEN count(t) > 0 THEN true ELSE false END FROM  OttoVariant t WHERE t.name = :name")
    public boolean isNameReserved(@Param("name") String title);

    @Query("SELECT CASE WHEN count(t) > 0 THEN true ELSE false END FROM  OttoVariant t WHERE t.name = :name and t.id<> :excludedId")
    public boolean isNameReserved(@Param("name") String title, @Param("excludedId") int excludedId);


    @Query("SELECT t.enabled FROM  OttoVariant t WHERE t.id=:variantId")
    boolean isEnabled(@Param("variantId") int variantId);


    @Query("SELECT CASE WHEN " +
            "EXISTS (SELECT 1 FROM OttoCharacter t WHERE t.variant.id = :variantId) " +
            "THEN TRUE ELSE FALSE END")
    boolean isReferencedElsewhere(@Param("variantId") int variantId);
}


